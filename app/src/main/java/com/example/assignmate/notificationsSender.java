package com.example.assignmate;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;

import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;

public class notificationsSender {
    String userToken,title,body;
    Context context;
    Activity activity;

    private RequestQueue requestQueue;
    private final String postUrl="";
    private final String serverKey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBzfRBp9vcJ+GQ\\nLzpp7QIbpAd2+MSbOePc7fgfULif7rLkPLNXgtoqqX8NrXAtplngkrT745Msl9z0\\nYEY1HfiRQtkRe7Dfb7AR/JzQtW0R4x+euz9J/AiNEv2jm/o9Anbc0r8KrWkCb9pS\\n0Re8l/S1twlIFWQDsXbzaAEO+5RBO1eXBFd30eCSnWcVf8YNspMCHisY7q/esatG\\n+RYxkxN4ZkUfxXQkQQPyGxIEBxcQw5H96HMYRtVL4q26UT9hCx93AgoMtKlsyzM7\\nm9wrdQWAPkjdqOPT9qtTNF3s6SjJlY34OQfMjvUEhbd9o5jfisMeek8GEqPQZhoy\\n4tKRAJ7PAgMBAAECggEACgliduv8hOBD3DW17rppe4j5If4sIGXVGSHx0Z1akl3I\\nQe2q1ANgzQjOHaU3xAVzbh/q44ibnJmYxccem62bq9osZ61iMZAVLDlK3bwnk9+R\\nSaUk1+4He1bab9iqEvLHuRPo9OKaJ5gnVFajxg6Qi8CY6HTDIzXTlTNzqpT4zo/v\\nCQaPucZ4EIkKtenzHVR9uRRJ8nM8/ULuoSZ07zdkZFKHFrVFdTqBmlATebzaWOBJ\\nOYMrE8BHQEI4yGjiEyPdzT2u+b1XMU09jNq7cu7LFux7rQk/G7sVDCJqU1suBifd\\nHlwXV7M2yeJPc1+Cipchiy8VCJ2umJFcMbXKXF9PcQKBgQDx6smNdClUqgfkMXbA\\nOEH7T3RC3oEljC4CjNxbWi0RZ3XU3QC5NjG37N6XP8aopK8w6CBrlHBg9WwoboNc\\nqzedayp/qKpxfOWBhl+sMQ+74CoR2fAP5basZxO8EPAjP6495J5ELoKjh6Ud22eC\\nQ0yXoZ+wJlRFdoUqJNrWCE1H7QKBgQDNFijufuRMBXbx2PkbAfi8FhNthx+XQhO7\\nunucZLl1ywY0ux+INILwincG7KfpPPcbGpQ953NEefQamClq7hlu9H+TfE27Icwi\\nsNbKU9WFiS5rGHhm8KY3ZqlsLB0fYon9FvuimJNL0sgPoDN+/nlLcfH8k89S3gfM\\n+dFfLjZyKwKBgQDJj2HXJc256OEiU8Qpa7Yt+YlYnaLk8eLz5QFEa+HY+mbGsEyG\\nBI1wPT9Ira6LnIsLKJy6hbcMUe+H0GxKra1sz3ldW96HKBTg0mCsS0RcuOzUas0F\\nsdN92XQr4cDy3YADuZPUIxxdvArq0X9wnToXrGYskgLszU8uSf67Dbe2EQKBgEDs\\nkfqmawlx9WysnnehUSfh6TaUPzj4Lx0P9Q/Whyu2IPnSfzH0Rbt2aiXkB2f759JJ\\nwMI0IjZ8UP2WTA5Q3/ZTKov7HvRtx5z31JdqoL7MZch7j6ou5r/FHT9kW7fSzxIz\\nE2gu2vp2+OSRd5vtXWK36L5n4ItARMHsGA8EpzHPAoGAI/OSSInq2XEUNx+Ds/9a\\nNR9IxYiZrwgk90X9/WfUxNqrMJ6gfkh9DY7nl9QYpJIkNV4kenRIVCC8UysNDf8E\\n7HKsrTZ58TA5ihW501RuUV0FyS5PCONewlD7Cd5Am9zU9tqUk7w+2kn04cQoQyKJ\\nE/rZzR76XQxjCZiEh25DLgM=\\n";

    public notificationsSender(String userToken, String title, String body, Context context, Activity activity) {
        this.userToken = userToken;
        this.title = title;
        this.body = body;
        this.context = context;
        this.activity = activity;
    }
}
