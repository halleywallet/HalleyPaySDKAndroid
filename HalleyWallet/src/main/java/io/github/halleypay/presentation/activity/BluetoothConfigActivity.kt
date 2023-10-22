package io.github.halleypay.presentation.activity

import io.github.halleypay.core.ui.BaseActivity

//
//import android.Manifest.permission.ACCESS_COARSE_LOCATION
//import android.Manifest.permission.ACCESS_FINE_LOCATION
//import android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
//import android.Manifest.permission.BLUETOOTH_CONNECT
//import android.Manifest.permission.BLUETOOTH_SCAN
//import android.Manifest.permission.READ_EXTERNAL_STORAGE
//import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.location.LocationManager
//import android.os.Build
//import android.os.Bundle
//import android.os.Parcelable
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.ActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.core.content.ContextCompat
//import com.google.android.material.snackbar.Snackbar
//import ir.github.halleywallet.core.ui.BaseActivity
//import ir.github.halleywallet.databinding.ActivityBlurtoothConfigBinding
//import ir.github.halleywallet.presentation.utils.ConnectThread
//import ir.github.halleywallet.presentation.utils.Constants.PAIRING_REQUEST
//import ir.github.halleywallet.presentation.utils.Constants.ROOT_DIRECTORY
//import ir.github.halleywallet.presentation.utils.Constants.TAG
//import ir.github.halleywallet.presentation.utils.Constants.TARGET_SSID
//import java.io.BufferedInputStream
//import java.io.File
//import java.io.FileInputStream
//import java.io.OutputStream


class BluetoothConfigActivity : BaseActivity() {

//    private val binding by lazy { ActivityBlurtoothConfigBinding.inflate(layoutInflater) }
//
//    private lateinit var bluetoothAdapter: BluetoothAdapter
//
//    private lateinit var connectThread: ConnectThread
//
//    private var indefiniteDiscoverBluetooth = false
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    private val permissionsLocation = arrayOf(
//        ACCESS_FINE_LOCATION,
//        ACCESS_COARSE_LOCATION,
//        ACCESS_LOCATION_EXTRA_COMMANDS,
//        BLUETOOTH_SCAN,
//        BLUETOOTH_CONNECT,
//        READ_EXTERNAL_STORAGE,
//        WRITE_EXTERNAL_STORAGE
//    )
//
//    private var requestPowerOnBluetooth =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            handleResultForStatusBluetooth(result)
//        }
//
//    private val requestMultiplePermissions =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            handleResultForGetPermission(permissions)
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        initBluetooth()
//        initButton()
//    }
//
//    private fun initBluetooth() {
//        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
//        bluetoothAdapter = bluetoothManager.adapter
//
//        val filter = IntentFilter()
//
//        filter.addAction(BluetoothDevice.ACTION_FOUND)
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        filter.addAction(PAIRING_REQUEST)
//
//        registerReceiver(receiverBluetooth, filter)
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun initButton() {
//        binding.btnDiscoverBluetooth.setOnClickListener {
//            when {
//                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) && !isPermissionsBluetoothGranted() -> {
//                    requestMultiplePermissions.launch(permissionsLocation)
//                    return@setOnClickListener
//                }
//
//                !isGpsEnabled() -> {
//                    showSnackbar("Please turn on your Location")
//                }
//
//                isBluetoothEnabled() -> {
//                    if (!bluetoothAdapter.isDiscovering) {
//                        indefiniteDiscoverBluetooth = true
//                        bluetoothAdapter.startDiscovery()
//                    }
//                    return@setOnClickListener
//                }
//            }
//        }
//
//        binding.btnCancelDiscoverBluetooth.setOnClickListener {
//            if (bluetoothAdapter.isDiscovering) {
//                indefiniteDiscoverBluetooth = false
//                bluetoothAdapter.cancelDiscovery()
//            }
//        }
//    }
//
//    private fun isBluetoothEnabled(): Boolean {
//        if (!bluetoothAdapter.isEnabled) {
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            requestPowerOnBluetooth.launch(enableBtIntent)
//            return false
//        }
//        return true
//    }
//
//    private fun handleResultForStatusBluetooth(result: ActivityResult) {
//        if (result.resultCode != RESULT_OK) {
//            showSnackbar("Please turn on your Bluetooth")
//        }
//    }
//
//    private fun handleResultForGetPermission(permissions: Map<String, @JvmSuppressWildcards Boolean>) {
//        permissions.entries.forEachIndexed { index, element ->
//            Log.d(TAG, "Permission $index ${element.key} = ${element.value}")
//            if (!element.value) {
//                showSnackbar("We need your permission")
//                return@forEachIndexed
//            }
//        }
//    }
//
//    private val receiverBluetooth: BroadcastReceiver = object : BroadcastReceiver() {
//        @SuppressLint("MissingPermission", "SetTextI18n")
//        override fun onReceive(context: Context?, intent: Intent) {
//
//            when (intent.action) {
//                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
//                    binding.pbLoading.visibility = View.VISIBLE
//                }
//
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                    if (indefiniteDiscoverBluetooth) {
//                        bluetoothAdapter.startDiscovery()
//                    } else {
//                        binding.pbLoading.visibility = View.GONE
//                    }
//                }
//
//                BluetoothDevice.ACTION_FOUND -> {
//                    val device =
//                        intent.getParcelableExtra<Parcelable>(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice?
//
//                    binding.lblDeviceList.text =
//                        "${binding.lblDeviceList.text} \n  Device Founded: ${device!!.name} : ${device.address}"
//
//                    if (device.name?.contains(TARGET_SSID) == true) {
//                        bluetoothAdapter.cancelDiscovery()
//                        connectThread = ConnectThread(device)
//                        connectThread.run()
//                        sendDataToDevice()
//                        indefiniteDiscoverBluetooth = false
//                    }
//                }
//
//                PAIRING_REQUEST -> {
//                    Toast.makeText(
//                        this@BluetoothConfigActivity,
//                        "PAIRING_REQUEST",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun isPermissionsBluetoothGranted(): Boolean {
//        return ((ContextCompat.checkSelfPermission(
//            this,
//            ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED)
//                && (ContextCompat.checkSelfPermission(
//            this,
//            ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED)
//                && (ContextCompat.checkSelfPermission(
//            this,
//            ACCESS_LOCATION_EXTRA_COMMANDS
//        ) == PackageManager.PERMISSION_GRANTED)
//                && (ContextCompat.checkSelfPermission(
//            this,
//            BLUETOOTH_SCAN
//        ) == PackageManager.PERMISSION_GRANTED)
//                && (ContextCompat.checkSelfPermission(
//            this,
//            BLUETOOTH_CONNECT
//        ) == PackageManager.PERMISSION_GRANTED)
//                && (ContextCompat.checkSelfPermission(
//            this,
//            WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED))
//    }
//
//    fun isPermissionGranted(permission: String): Boolean {
//        return checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun isGpsEnabled(): Boolean {
//        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
//        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//    }
//
//    @SuppressLint("LogNotTimber", "MissingPermission")
//    private fun sendDataToDevice() {
//        var outStream: OutputStream?
//        try {
//            outStream = connectThread.mSocket?.outputStream
//            val directory = getExternalFilesDir(ROOT_DIRECTORY)
//            val transferFile = File(directory?.path + "/informationForSend.txt")
//            if (!transferFile.exists()) {
//                transferFile.createNewFile()
//            }
//            val myByteArray = ByteArray(transferFile.length().toInt())
//            val fis = FileInputStream(transferFile)
//            val bis = BufferedInputStream(fis, 1272254)
//            bis.read(myByteArray, 0, myByteArray.size)
//            outStream?.write(myByteArray, 0, myByteArray.size)
//            outStream?.flush()
//            outStream?.close()
//            showSnackbar("Transfer data To ${connectThread.device.name} is Successful")
//        } catch (e: Exception) {
//            showSnackbar(e.message.toString())
//        }
//    }
//
//    private fun showSnackbar(message: String) {
//        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
//            .setAnchorView(binding.btnDiscoverBluetooth)
//            .show()
//    }
//
//    @SuppressLint("MissingSuperCall")
//    override fun onBackPressed() {
//        onBackPressedDispatcher.onBackPressed()
//        finish()
//    }
//
//    override fun onDestroy() {
//        unregisterReceiver(receiverBluetooth)
//        connectThread.cancel()
//        super.onDestroy()
//    }
}