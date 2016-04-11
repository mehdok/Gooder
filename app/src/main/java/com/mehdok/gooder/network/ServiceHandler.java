/*
 * Copyright (c) 2016. Mehdi Sohrabi
 */

package com.mehdok.gooder.network;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServiceHandler
{
    private final String USER_AGENT = "Mozilla/5.0";

    public ServiceHandler()
    {

    }

    /**
     * Making service call
     *
     * @url - url to make request
     * @method - http request method
     */
    public String makeServiceCall(String url)
    {
        try
        {
            return sendGet(url);
        } catch (Exception e)
        {
            e.printStackTrace();
            return "[]";
        }
    }

    // HTTP GET request
    private String sendGet(String url) throws Exception
    {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        return (response.toString());
    }

    public String sendHttpsGet(String urlString) throws Exception
    {
        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        return (response.toString());
    }

    public String makePostRequest(Context context, String url, String query)
    {
        try
        {
            return sendPost(url, query);
        } catch (Exception e)
        {
            e.printStackTrace();
            return "[]";
        }
    }

    // HTTP POST request
    private String sendPost(String url, String query) throws Exception
    {
        URL u = new URL(url);
        HttpURLConnection uc = (HttpURLConnection) u.openConnection();
        uc.setDoOutput(true);
        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
        uc.setRequestProperty("Content-Length", String.valueOf(query.length()));
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        uc.setRequestProperty("Accept", "*/*");
        uc.setRequestMethod("POST");
        uc.setInstanceFollowRedirects(false); // very important line :)
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(uc.getOutputStream()), true);
        pw.print(query);
        pw.flush();
        pw.close();
        int responseCode = uc.getResponseCode();

        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(uc.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Response: " + response);

        return response.toString();
    }
}
