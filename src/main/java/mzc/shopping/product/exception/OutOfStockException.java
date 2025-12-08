package mzc.shopping.product.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(Long productId, int requested, int available) {
        super("재고가 부족합니다. 상품ID: " + productId +
                ", 요청수량: " + requested +
                ", 현재재고: " + available);
    }
}