package kr.flab.tradingmarket.domain.room.service;

import static kr.flab.tradingmarket.domain.chat.util.RedisKeyUtils.*;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.flab.tradingmarket.domain.product.entity.Product;
import kr.flab.tradingmarket.domain.product.service.ProductService;
import kr.flab.tradingmarket.domain.room.dto.response.ResponseRoomDto;
import kr.flab.tradingmarket.domain.room.entity.RoomKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultRoomFacadeService implements RoomFacadeService {
    private final RoomService roomService;
    private final ProductService productService;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseRoomDto> findRooms(Long userNo) {
        Set<String> roomIdList = roomService.findByUserId(userNo);
        if (roomIdList.size() == 0) {
            return null;
        }
        List<Long> productNoList = roomIdList.stream().map(this::getRoomId).toList();

        return productService.findProductAndSellerByNoList(productNoList).stream()
            .map(i -> new ResponseRoomDto(createRoomId(userNo, i.getSeller().getUserNo(), i.getProductNo()),
                i.getProductNo(),
                i.getSeller().getUserName(), i.getProductName())).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public String createRooms(RoomKey roomKey) {
        Product findProduct = productService.findById(roomKey.getProductNo());
        Long sellerNo = findProduct.getSeller().getUserNo();
        Long productNo = findProduct.getProductNo();
        //Todo 쓰기 스큐 발생 가능성 있음
        roomKey.isValid(sellerNo, productNo);

        return roomService.createRooms(roomKey);
    }

    private Long getRoomId(String i) {
        return Long.valueOf(i.split(":")[2]);
    }
}
