package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;

/**
 * IEC102规范的帧不论定长还是变长都必须实现的约束。
 *
 * @author KanouAkira
 * @date 2022/4/28 10:18
 */
public interface Iec102MessageDetail {

    int getFcb();

    FunctionCodeEnum getFunctionCodeEnum();

}
