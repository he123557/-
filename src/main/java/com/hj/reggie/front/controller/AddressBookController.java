package com.hj.reggie.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hj.reggie.commons.bean.R;
import com.hj.reggie.commons.utils.BaseContextUtil;
import com.hj.reggie.front.bean.AddressBook;
import com.hj.reggie.front.bean.User;
import com.hj.reggie.front.service.AddressBookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @create 2023-03-12 17:09
 */
@Controller
public class AddressBookController {
    @Resource
    private AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping("/addressBook")
    @ResponseBody
    public Object addAddress(@RequestBody AddressBook addressBook) {
        Long userId = BaseContextUtil.getCurrentId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success("地址添加成功");
    }

    /**
     * 查询指定用户的所有地址
     *
     * @return
     */
    @GetMapping("/addressBook/list")
    @ResponseBody
    public Object listAddress() {
        Long userId = BaseContextUtil.getCurrentId();
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(userId != null, AddressBook::getUserId, userId).orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookService.list(addressBookLambdaQueryWrapper));
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/addressBook/default")
    @ResponseBody
    public Object defaultAddress(@RequestBody AddressBook addressBook) {
        Long userId = BaseContextUtil.getCurrentId();
        //将该用户下所有地址的is_default设为0
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault, 0)
                .eq(userId != null, AddressBook::getUserId, userId);
        addressBookService.update(addressBookLambdaUpdateWrapper);
        //更改当前地址的is_default为1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 修改地址表单回显
     *
     * @param id
     * @return
     */
    @GetMapping("/addressBook/{id}")
    @ResponseBody
    public Object selectAddressById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook == null) {
            R.error("没有查询到该地址");
        }
        return R.success(addressBook);
    }

    /**
     * 保存修改的地址
     * @param addressBook
     * @return
     */
    @PutMapping("/addressBook")
    @ResponseBody
    public Object saveUpdateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }

    /**
     * 结账时回显地址
     * @return
     */
    @GetMapping("/addressBook/default")
    @ResponseBody
    public Object showDefaultAddress(){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId,BaseContextUtil.getCurrentId())
                .eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);
        if (addressBook==null){
            return R.error("没查到地址，请添加地址");
        }else {
            return R.success(addressBook);
        }
    }
}
