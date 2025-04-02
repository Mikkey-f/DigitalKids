package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mapper.UserAddressMapper;
import com.digital.model.entity.UserAddress;
import com.digital.service.UserAddressService;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【user_address】的数据库操作Service实现
* @createDate 2025-03-28 15:27:45
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService {

}




