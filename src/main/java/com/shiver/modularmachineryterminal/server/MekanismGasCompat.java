package com.shiver.modularmachineryterminal.server;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;

/**
 * 仅在 Mekanism 已加载时调用，避免让机器缓存直接依赖可选模组的类型。
 */
final class MekanismGasCompat {

    private MekanismGasCompat() {
    }

    static String getName(Object gasStack) {
        if (!(gasStack instanceof GasStack)) {
            return "";
        }
        Gas gas = ((GasStack) gasStack).getGas();
        // 不使用 Class.getMethod：解析 Gas 的方法签名可能加载仅客户端存在的 TextureMap。
        return gas == null ? "" : String.valueOf(gas.getLocalizedName());
    }
}
