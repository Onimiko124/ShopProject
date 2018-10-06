package com.qf.service;


import com.qf.entity.Address;

import java.util.List;

public interface IAddressService {

    List<Address> queryAddress(Integer uid);

    Address queryByid(Integer id);

    Address addAddress(Address address);
}
