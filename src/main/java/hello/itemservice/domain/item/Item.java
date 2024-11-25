package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

// @Data 는 예측할 수 없게 사용될 수 있기 때문에 위험하다. 필요한 것들만(Getter, Setter) 분리해서 사용하기 / DTO 에는 @Data 를 사용해도 괜찮다.
@Getter
@Setter
public class Item {

    private Long id;
    private String itemName;
    /**
     * int 대신 Integer 를 사용한 이유
     *  : 없을 수도 있기 때문(null 이 들어갈 수 있도록) / int 는 0 이라도 꼭 들어가야한다.
     */
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
