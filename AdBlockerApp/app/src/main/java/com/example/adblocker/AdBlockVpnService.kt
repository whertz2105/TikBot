package com.example.adblocker

import android.net.VpnService
import android.net.VpnService.Builder
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Simple VPN-based ad blocker. This is a very basic example and does not
 * implement a full ad blocking engine. It shows how to start a VPN service
 * that could filter traffic using a predefined host list.
 */
class AdBlockVpnService : VpnService() {
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        setupVpn()
        return START_STICKY
    }

    private fun setupVpn() {
        val builder = Builder()
        builder.addAddress("10.0.0.2", 32)
        builder.addRoute("0.0.0.0", 0)
        vpnInterface = builder.setSession("AdBlocker").establish()
        vpnInterface?.let {
            GlobalScope.launch(Dispatchers.IO) {
                runVpnLoop(it)
            }
        }
    }

    private fun runVpnLoop(fd: ParcelFileDescriptor) {
        val inputStream = FileInputStream(fd.fileDescriptor)
        val outputStream = FileOutputStream(fd.fileDescriptor)
        val buffer = ByteArray(32767)
        while (true) {
            val length = inputStream.read(buffer)
            if (length > 0) {
                // TODO: Inspect packets and drop connections to ad servers
                outputStream.write(buffer, 0, length)
            } else {
                Thread.sleep(10)
            }
        }
    }

    override fun onDestroy() {
        vpnInterface?.close()
        super.onDestroy()
    }
}
