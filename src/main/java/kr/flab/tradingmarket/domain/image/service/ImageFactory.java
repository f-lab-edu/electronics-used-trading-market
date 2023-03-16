package kr.flab.tradingmarket.domain.image.service;

import kr.flab.tradingmarket.domain.image.entity.BaseImage;

@FunctionalInterface
public interface ImageFactory {
    BaseImage create(String originalFileName, String fileLink, long fileSize);
}