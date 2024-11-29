package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    
    private final ItemRepository itemRepository;

    /* @RequiredArgsConstructor 를 사용함으로써 final 이 붙은 field 에 대해서 아래 주석 부분 자동으로 수행
    @Autowired       ← 생성자가 하나일 때, 생략 가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }*/

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * 같은 URL 인데, HTTP 메서드로 기능을 구분하는 방식 선호
     */
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemName,
                            @RequestParam("price") int price,
                            @RequestParam("quantity") Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {       // @ModelAttribute : Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(set___)으로 입력해준다. & Model 생략 가능

        itemRepository.save(item);
//        model.addAttribute("item", item);     자동 추가, 생략 가능
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {       // @ModelAttribute name 생략 시, class 이름에서 첫 글자만 소문자로 바꾼 단어로 name 사용(Item → item)

        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {       // String 같은 단순 type 은 @RequestParam 이 적용되지만, 직접 만든 임의의 객체인 경우 @ModelAttribute 가 적용되기 때문에 생략 가능

        itemRepository.save(item);
        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV5(Item item) {

        itemRepository.save(item);
        /**
         * PRG 패턴 : Post → Redirect → Get
         * V4와 같이 상품 등록(add_post) 후 template 으로 이동하면 화면은 상세 페이지지만, url 은 /basic/items/add 인 상태이다.
         * 그 상태에서 새로고침을 계속 하게되면 똑같은 상품이 id만 증가한 채 계속 둥록된다. (웹 브라우저의 새로고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.)
         * 이 이슈를 해결하기 위해 redirect 를 사용해서 실제 상품 상세 페이지로(/basic/item/{itemId}) 이동하게 한다.
         */
        return "redirect:/basic/items/" + item.getId();
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
