package com.infolai.liquorder.repositories;

import androidx.lifecycle.MutableLiveData;

import com.infolai.liquorder.enums.Status;
import com.infolai.liquorder.models.BaseItem;
import com.infolai.liquorder.models.MenuItem;
import com.infolai.liquorder.models.Order;
import com.infolai.liquorder.models.OrderItem;
import com.infolai.liquorder.models.httpparams.CreateOrderRequest;
import com.infolai.liquorder.models.httpparams.SaveOrderItemRequest;
import com.infolai.liquorder.utilities.Util;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private static CartRepository cartRepository;

    private Order order;
    private MutableLiveData<List<MenuItem>> menuItems = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<OrderItem>> orderItems = new MutableLiveData<>(new ArrayList<>());
    private List<OrderItem> orderSnapshot;

    private CartRepository() { }

    public static CartRepository getInstance() {
        if (cartRepository == null) cartRepository = new CartRepository();

        return cartRepository;
    }

    public boolean isEmpty() {
        boolean result = (menuItems.getValue().size() == 0);

        return (result && !isOrderItemsChanged());
    }

    private boolean isOrderItemsChanged() {
        boolean result = false;
        List<OrderItem> orderItemList = orderItems.getValue();

        if (orderSnapshot != null && orderSnapshot.size() > 0) {
            if (orderSnapshot.size() == orderItemList.size()) {
                for (OrderItem sItem : orderSnapshot) {
                    OrderItem item = findOrderItemByLiquorId(sItem.liquorId);

                    if (item == null || item.quantity != sItem.quantity) {
                        result = true;
                        break;
                    }
                }
            } else {
                result = true;
            }
        }

        return result;
    }

    public void clearCart() {
        order = null;
        menuItems.setValue(new ArrayList<>());
        orderItems.setValue(new ArrayList<>());
        orderSnapshot = null;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order o, List<OrderItem> itemList) {
        Status status = Status.valueOf(o.orderStatus);

        clearCart();
        order = o;
        orderItems.setValue(itemList);
        if (status == Status.SAVE) snapshoot(itemList);
    }

    public void snapshoot(List<OrderItem> itemList) {
        orderSnapshot = new ArrayList<>();

        for (OrderItem item : itemList) {
            orderSnapshot.add(item.clone());
        }
    }

    public List<BaseItem> getTotalItemList() {
        List<BaseItem> result = new ArrayList<>();

        result.addAll(menuItems.getValue());
        result.addAll(orderItems.getValue());

        return result;
    }

    public int getTotalAmount() {
        int result = 0;
        List<BaseItem> totalItemList = getTotalItemList();

        for (BaseItem item : totalItemList) {
            result += item.price * item.quantity;
        }

        return result;
    }

    public int getQuantity(int liquorId) {
        int result;
        OrderItem orderItem = findOrderItemByLiquorId(liquorId);

        if (orderItem != null) {
            result = orderItem.quantity;
        } else {
            MenuItem menuItem = findMenuItemByLiquorId(liquorId);

            result = (menuItem != null) ? menuItem.quantity : 0;
        }

        return result;
    }

    public int addMenuItem(MenuItem item) {
        int result = addOrderItem(item);

        if (result == 0) {
            MenuItem orgItem = findMenuItemByLiquorId(item.liquorId);

            if (orgItem == null) {
                menuItems.getValue().add(item.clone());
            } else {
                orgItem.quantity += item.quantity;
            }

            result = (orgItem == null) ? item.quantity : orgItem.quantity;
        }

        return result;
    }

    private int addOrderItem(MenuItem item) {
        OrderItem orgItem = findOrderItemByLiquorId(item.liquorId);

        if (orgItem != null) orgItem.quantity += item.quantity;

        return (orgItem != null) ? orgItem.quantity : 0;
    }

    public void updateOrderItem(BaseItem item) {
        OrderItem orderItem = findOrderItemByLiquorId(item.liquorId);

        if (orderItem == null) {
            MenuItem menuItem = findMenuItemByLiquorId(item.liquorId);

            if (menuItem != null) {
                if (item.quantity == 0) {
                    menuItems.getValue().remove(menuItem);
                } else {
                    menuItem.quantity = item.quantity;
                }
            }
        } else {
            if (item.quantity == 0) {
                orderItems.getValue().remove(orderItem);
            } else {
                orderItem.quantity = item.quantity;
            }
        }
    }


    private MenuItem findMenuItemByLiquorId(int liquorId) {
        List<MenuItem> itemList = menuItems.getValue();
        MenuItem result = null;

        for (MenuItem item : itemList) {
            if (item.liquorId == liquorId) {
                result = item;
                break;
            }
        }

        return result;
    }

    public CreateOrderRequest getCreateOrderRequest(int cellarerId) {
        CreateOrderRequest result = new CreateOrderRequest();

        result.cellarerId = cellarerId;
        result.orderitemRequest = new SaveOrderItemRequest();
        result.orderitemRequest.itemsToUpdate = getOrderItemArrayFromMenuItems();

        return result;
    }

    public SaveOrderItemRequest getSaveOrderItemRequest() {
        SaveOrderItemRequest result = new SaveOrderItemRequest();
        List<OrderItem> updateOrderItemList = new ArrayList<>();
        List<Integer> deletedIdList = new ArrayList<>();

        getNewOderItem(updateOrderItemList);
        getUpdatedAndDeletedOrderItem(updateOrderItemList, deletedIdList);
        result.itemsToUpdate = new OrderItem[updateOrderItemList.size()];
        result.itemIdsToDelete = new int[deletedIdList.size()];
        updateOrderItemList.toArray(result.itemsToUpdate);
        Util.IntList2IntArray(deletedIdList, result.itemIdsToDelete);

        return result;
    }

    private void getUpdatedAndDeletedOrderItem(List<OrderItem> updateOrderItemList, List<Integer> deletedIdList) {
        for (OrderItem orgItem : orderSnapshot) {
            OrderItem item = findOrderItemByLiquorId(orgItem.liquorId);

            if (item == null) {
                deletedIdList.add(orgItem.id);
            } else if (item.quantity != orgItem.quantity) {
                updateOrderItemList.add(item);
            }
        }
    }

    private void getNewOderItem(List<OrderItem> updateOrderItemList) {
        for (MenuItem item : menuItems.getValue()) {
            updateOrderItemList.add(item.toOrderItem());
        }
    }


    private OrderItem[] getOrderItemArrayFromMenuItems() {
        List<MenuItem> menuItemList = menuItems.getValue();
        OrderItem[] result = new OrderItem[menuItemList.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = menuItemList.get(i).toOrderItem();
        }

        return result;
    }

    private OrderItem findOrderItemByLiquorId(int liquorId) {
        List<OrderItem> itemList = orderItems.getValue();
        OrderItem result = null;

        for (OrderItem item : itemList) {
            if (item.liquorId == liquorId) {
                result = item;
                break;
            }
        }

        return result;
    }
}
