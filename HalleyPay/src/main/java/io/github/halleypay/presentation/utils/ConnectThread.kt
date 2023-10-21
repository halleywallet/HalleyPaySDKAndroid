package io.github.halleypay.presentation.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException

@SuppressLint("MissingPermission")
class ConnectThread(val device: BluetoothDevice) : Thread() {

    val mSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
    }

    override fun run() {
        mSocket?.let { socket ->
            socket.connect()
        }
    }

    fun cancel() {
        try {
            mSocket?.close()
        } catch (_: IOException) {
        }
    }
}