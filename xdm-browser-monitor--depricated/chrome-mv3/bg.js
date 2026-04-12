"use strict";

// XDM MV3 Service Worker Background Script
// Uses chrome.action, chrome.storage.session, and native messaging with reconnection logic

var xdmHost = "http://127.0.0.1:9614";
var port = null;
var reconnectDelay = 1000;
var maxReconnectDelay = 30000;
var monitoring = true;

function log(msg) {
    console.log("[XDM MV3]", msg);
}

// Native messaging with reconnection logic
function connectNativeHost() {
    try {
        port = chrome.runtime.connectNative("xdm_chrome.native_host");
        port.onDisconnect.addListener(function () {
            log("Native host disconnected, reconnecting in " + reconnectDelay + "ms");
            port = null;
            setTimeout(function () {
                reconnectDelay = Math.min(reconnectDelay * 2, maxReconnectDelay);
                connectNativeHost();
            }, reconnectDelay);
        });
        port.onMessage.addListener(function (message) {
            log("Native host message: " + JSON.stringify(message));
            reconnectDelay = 1000; // Reset delay on successful message
        });
        log("Connected to native host");
    } catch (e) {
        log("Failed to connect to native host: " + e);
        port = null;
    }
}

function postNativeMessage(message) {
    if (port) {
        try {
            port.postMessage(message);
        } catch (e) {
            log("Failed to post message: " + e);
            port = null;
            connectNativeHost();
        }
    } else {
        sendViaHttp(message);
    }
}

function sendViaHttp(data) {
    try {
        var text = "";
        if (data.message) {
            var msg = data.message;
            text += "url=" + msg.url + "\r\n";
            if (msg.file) text += "file=" + msg.file + "\r\n";
        }
        var endpoint = data.messageType === "video" ? "/video" : "/download";
        fetch(xdmHost + endpoint, {
            method: "POST",
            body: text
        }).catch(function (e) {
            log("HTTP send failed: " + e);
        });
    } catch (e) {
        log("sendViaHttp error: " + e);
    }
}

// Icon state management using chrome.storage.session
function setIcon(state) {
    var icon = state === "active" ? "icon.png" : "icon_disabled.png";
    chrome.action.setIcon({ path: icon });
    chrome.storage.session.set({ iconState: state });
}

function syncSettings() {
    fetch(xdmHost + "/sync")
        .then(function (res) { return res.json(); })
        .then(function (data) {
            monitoring = data.monitoring !== false;
            setIcon(monitoring ? "active" : "disabled");
        })
        .catch(function (e) {
            log("Sync failed: " + e);
            setIcon("disabled");
        });
}

// Download interception
chrome.webRequest.onHeadersReceived && chrome.webRequest.onHeadersReceived.addListener(
    function (details) {
        // Handled via declarativeNetRequest in MV3 or service worker
    },
    { urls: ["<all_urls>"] }
);

chrome.action.onClicked.addListener(function (tab) {
    fetch(xdmHost + "/cmd", { method: "POST" }).catch(function () {});
});

// Initialize
connectNativeHost();
syncSettings();

log("XDM MV3 service worker started");
