package com.qf.dao;

import com.qf.entity.Address;

import java.util.List;

public interface IAddressDao {

    List<Address> queryAddress(Integer uid);

    Address queryAddressByid(Integer id);

    int addAddress(Address address);
}
