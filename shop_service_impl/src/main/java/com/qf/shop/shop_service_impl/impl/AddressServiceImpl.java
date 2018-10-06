package com.qf.shop.shop_service_impl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IAddressDao;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private IAddressDao addressDao;

    @Override
    public List<Address> queryAddress(Integer uid) {
        return addressDao.queryAddress(uid);
    }

    @Override
    public Address queryByid(Integer id) {
        return addressDao.queryAddressByid(id);
    }

    @Override
    public Address addAddress(Address address) {
        int addressid = addressDao.addAddress(address);
        address.setId(addressid);
        return address;
    }
}
