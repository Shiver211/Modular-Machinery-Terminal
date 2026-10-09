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
        // getLocalizedName() has a client-only return type in some Mekanism builds.
        // getName() is server-safe and still identifies the gas for the terminal.
        return gas == null ? "" : gas.getName();
    }
}
