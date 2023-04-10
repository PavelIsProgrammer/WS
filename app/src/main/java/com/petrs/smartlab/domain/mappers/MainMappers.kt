package com.petrs.smartlab.domain.mappers

import com.petrs.smartlab.data.models.CatalogItemDTO
import com.petrs.smartlab.data.models.NewsItemDTO
import com.petrs.smartlab.domain.models.CatalogItemDomain
import com.petrs.smartlab.domain.models.NewsItemDomain

fun CatalogItemDTO.toDomain() =
    CatalogItemDomain(
        id = id,
        title = title,
        description = description,
        price = price,
        category = category,
        timeResult = timeResult,
        preparation = preparation,
        bio = bio,
        inCart = inCart
    )

fun NewsItemDTO.toDomain() =
    NewsItemDomain(
        id = id,
        title = title,
        description = description,
        price = price,
        image = image
    )

fun CatalogItemDomain.toDTO() =
    CatalogItemDTO(
        id = id,
        title = title,
        description = description,
        price = price,
        category = category,
        timeResult = timeResult,
        preparation = preparation,
        bio = bio,
        inCart = inCart
    )