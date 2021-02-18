package com.xxx;

import com.alibaba.fastjson.JSONObject;
import com.xxx.dto.OpenOrder;
import com.xxx.enums.OrderSide;
import com.xxx.enums.OrderState;
import com.xxx.utils.DateTime;
import com.xxx.websocket.MyWebSocketClient;
import com.xxx.websocket.listener.AccountListener;
import com.xxx.websocket.listener.AggTradeListener;
import com.xxx.websocket.listener.MessageListener;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Market {
    int BASE = 20;//最小持仓
    int MAX = 30;//最大持仓
    BigDecimal QUANTITY = new BigDecimal(1);//每次交易数量
    AggTradeListener priceHolder = new AggTradeListener();//监听价格变化
    Stock stock;
    Exchange exchange;

    OrderTradeUpdateListener orderTradeUpdateListener = new OrderTradeUpdateListener(this);
    AccountUpdateListener accountUpdateListener = new AccountUpdateListener();
    volatile OrderVo buying;
    volatile OrderVo selling;
    private List<MessageListener> orderListener;
    private List<AccountListener> accountListener;

    public static final String BUY = "B";
    public static final String SELL = "S";

    public Market() throws URISyntaxException, InterruptedException {
        exchange = new Exchange();
        orderListener = new ArrayList();
        orderListener.add(priceHolder);

        accountListener = new ArrayList();
        accountListener.add(orderTradeUpdateListener);
        accountListener.add(accountUpdateListener);

        stock = Stock.createStock(exchange);
        for (int i = 0; i < 10; i++) {
            int expectSells = stock.getHolds() - BASE;
            if (expectSells != stock.sells()) {
                log.info("持仓信息有误，重新发起:" + stock);
                Thread.sleep(1000L);
            } else {
                break;
            }
        }
        int expectSells = stock.getHolds() - BASE;
        if (expectSells != stock.sells()) {
            throw new RuntimeException(expectSells + "!=" + (stock.sells()) + ",当前持仓：" + stock + ":");
        }

        log.info("初始化系统，当前持仓：" + stock);
        printStock();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    doTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        while (true) {
            MyWebSocketClient client = createSocket(orderListener, accountListener);
            Thread.sleep(1000 * 60 * 60);
            client.close();
        }

    }

    private void printStock() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30 * 1000L);
                    if (stock.toString().equalsIgnoreCase(this.stock.toString())) {
                        log.info("当前持仓：" + stock);
                    } else {
                        log.info("远程持仓：" + stock + ",本地持仓：" + this.stock);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void doTrace() {
        if (stock.getHolds() + stock.buys() >= MAX) {
            List<OrderVo> last = stock.sellList();
            ;
            log.info("等待卖出：" + last.get(last.size() - 1).price);
            printCurrent();
            return;
        }
        BigDecimal pp = priceHolder.getPrice();
        if (pp == null) {
            return;
        }

        if (buying != null) {
            log.info("等待买入：" + buying);
            printCurrent();
            return;
        }

        if (selling != null) {
            log.info("等待卖出：" + selling);
            return;
        }

        int price = priceHolder.getPrice().intValue();
        OrderVo currentBuy = null;
        for (OrderVo buy : stock.buyList()) {
            if (price - buy.price <= area() * 1.5) {
                currentBuy = buy;
                break;
            }
        }
        if (currentBuy != null) {
            log.info("当前价格：" + priceHolder.getPrice() + ",等待买入价格:" + currentBuy.price);
            check();
            return;
        }


        OrderVo currentSell = null;
        for (OrderVo sell : stock.sellList()) {
            if (sell.price - price <= area() * 1.3) {
                currentSell = sell;
                break;
            }
        }

        if (currentSell != null) {
            log.info("当前价格：" + priceHolder.getPrice() + ",等待卖出:" + currentSell.price);
            return;
        }
        int buyPrice = price - area() / 2;
        String id = newId(BUY, price);
        exchange.order(OrderSide.BUY, new BigDecimal(buyPrice), QUANTITY, id);
        buying = OrderVo.newOrder(buyPrice + "", QUANTITY + "", id);
        log.info("下买单：" + buyPrice);

    }

    private void check() {
        List<OpenOrder> ors = exchange.openOrders();
        for (OpenOrder od : ors) {
            BigDecimal order = new BigDecimal(od.getPrice());
            BigDecimal limit = new BigDecimal(od.getPrice());
            //当前价格-订单价格>AREA*2
            if (priceHolder.getPrice().subtract(order).compareTo(new BigDecimal(area() * 2)) > 0) {
                if (od.getClientOrderId().startsWith("B")) {
                    exchange.cancel(od.getClientOrderId());
                    log.info("价格过低，取消买单：" + od.getClientOrderId());
                }
            }
        }
    }

    private void printCurrent() {
        log.info(stock + "-->" + priceHolder.getPrice());
    }


    private List<AccountListener> accountListeners() {
        List list = new ArrayList();
        list.add(orderTradeUpdateListener);
        list.add(accountUpdateListener);
        return list;
    }


    private static MyWebSocketClient createSocket(List<MessageListener> messageListeners, List<AccountListener> accountListeners) throws URISyntaxException, InterruptedException {
        Exchange exchange = new Exchange();
        MyWebSocketClient client = MyWebSocketClient.getInstance(exchange, messageListeners, accountListeners);
        client.start();
        return client;
    }

    private static String newId(String pix, int price) {
        return pix + DateTime.current().toString(DateTime.YEAR_TO_SECOND_STRING) + "-" + price;
    }

    public boolean orderChange(JSONObject order) {
        String status = order.getString("X");
        String orderId = order.getString("c");
        BigDecimal price = order.getBigDecimal("p");
        BigDecimal quantity = order.getBigDecimal("q");

        if (OrderState.NEW.toString().equalsIgnoreCase(status)) {
            if (orderId.startsWith(BUY)) {
                OrderVo newOrder = OrderVo.newOrder(price.toString(), quantity.toString(), orderId);

                if (newOrder.toString().equalsIgnoreCase(buying.toString())) {
                    stock.getBuys().put(orderId, newOrder);
                    buying = null;
                } else {
                    log.error("不认识的买单：" + newOrder + ",当前买单：" + buying);
                }

            }
            if (orderId.startsWith(SELL)) {
                OrderVo newOrder = OrderVo.newOrder(price.toString(), quantity.toString(), orderId);
                if (newOrder.toString().equalsIgnoreCase(selling.toString())) {
                    stock.getSells().put(orderId, newOrder);
                    selling = null;
                } else {
                    log.error("不认识的卖单：" + newOrder + ",当前卖单：" + selling);
                }
            }
        }

        if (OrderState.FILLED.toString().equalsIgnoreCase(status)) {
            if (orderId.startsWith(BUY)) {
                OrderVo vo = stock.getBuys().remove(orderId);
                stock.setHolds(stock.getHolds() + vo.quantity);
                log.info("买入：" + vo);
                //加价5卖出
                BigDecimal sellPrice = new BigDecimal(price.intValue() + 5);
                String sellId = newId(SELL, price.intValue());
                exchange.order(OrderSide.SELL, sellPrice, QUANTITY, sellId);
                selling = OrderVo.newOrder(sellPrice + "", QUANTITY + "", sellId);
                log.info("下卖单：" + sellPrice);
            }

            if (orderId.startsWith(SELL)) {
                OrderVo vo = stock.getSells().remove(orderId);
                if (vo == null) {
                    log.error("找不到卖单:" + stock + ":" + price);
                    return true;
                }
                stock.setHolds(stock.getHolds() - vo.quantity);
                log.info("卖出：" + vo);

            }

        }

        if (OrderState.CANCELED.toString().equalsIgnoreCase(status)) {
            if (orderId.startsWith(BUY)) {
                OrderVo vo = stock.getBuys().remove(orderId);
                log.info("取消订单：" + price.intValue() + ":" + vo);
                printCurrent();
            }

        }

        return true;
    }

    private int area() {
        return 4 + (stock.getHolds() - BASE) / 3;
    }
}
