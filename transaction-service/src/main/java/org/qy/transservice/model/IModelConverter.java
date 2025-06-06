package org.qy.transservice.model;

/**
 * 各层模型转换接口
 * (持久层po与业务层bo) (业务层bo与接入参数param/返回参数dto)
 *
 * @author qinyang
 * @date 2025/6/5 20:12
 */
public interface IModelConverter {

    default public Object poToBo(Object po){
        return null;
    }

    default public Object boToPo(Object bo){
        return null;
    }

    default public Object boToDto(Object bo){
        return null;
    }
}
