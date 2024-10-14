package com.example.jeudecarte.HereToSlay.network;

import static com.example.jeudecarte.HereToSlay.network.NsdServiceDiffuser.SERVICE_NAME;
import static com.example.jeudecarte.HereToSlay.network.NsdServiceDiffuser.SERVICE_TYPE;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;

public class NsdServiceReceiver {


    private NsdManager nsdManager;

    private NsdManager.ResolveListener resolveListener;

    //private String serviceName;

    public NsdServiceReceiver(Context context){
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void initializeDiscoveryListener() {
        // Instantiate a new DiscoveryListener
        NsdManager.DiscoveryListener discoveryListener = new NsdManager.DiscoveryListener() {
            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d("affichage debug", "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                Log.d("affichage debug", "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d("affichage debug", "Unknown Service Type: " + service.getServiceType());
//                } else if (service.getServiceName().equals(serviceName)) {
//                    // The name of the service tells the user what they'd be
//                    // connecting to. It could be "Bob's Chat App".
//                    Log.d("affichage debug", "Same machine: " + serviceName);
                } else if (service.getServiceName().contains(SERVICE_NAME)) {
                    nsdManager.resolveService(service, resolveListener);
                    Log.d("affichage debug", "Service found: " + service.getServiceName());
//                    Log.d("affichage debug", service.getHost().getHostAddress() + " " + service.getPort());
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e("affichage debug", "service lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i("affichage debug", "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("affichage debug", "Discovery failed: Error code:" + errorCode);
//                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("affichage debug", "Discovery failed: Error code:" + errorCode);
//                nsdManager.stopServiceDiscovery(this);
            }
        };

        nsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void initializeResolveListener(ArrayList<NsdServiceInfo> hostsInfo) {
        resolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e("affichage debug", "Resolve failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e("affichage debug", "Resolve Succeeded. " + serviceInfo);

//                if (serviceInfo.getServiceName().equals(serviceName)) {
//                    Log.d("affichage debug", "Same IP.");
//                    return;
//                }
                hostsInfo.add(serviceInfo);
                int port = serviceInfo.getPort();
                InetAddress host = serviceInfo.getHost();

            }
        };
    }
}
