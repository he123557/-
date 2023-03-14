package com.hj.reggie.front.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.front.bean.AddressBook;
import com.hj.reggie.front.mapper.AddressBookMapper;
import com.hj.reggie.front.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @create 2023-03-12 17:10
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
