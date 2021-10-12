package com.example.zebrascannerappdemo.domain.mappers

import com.example.zebrascannerappdemo.data.network.model.response.UserInfoResponse
import com.example.zebrascannerappdemo.data.storage.prefs.model.UserDto
import com.example.zebrascannerappdemo.domain.model.User

internal fun UserDto.toUser(): User = User(
    name = this.name,
    locationName = this.locationName,
)

internal fun UserInfoResponse.toUserDto(): UserDto = UserDto(
    name = this.name,
    locationName = this.locationName,
)