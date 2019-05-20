package com.example.hariharansivakumar.tike;

/**
 * Created by Hariharan Sivakumar on 2/16/2018.
 */

public class Constants {

    public static final String ROOT_URL =  "http://192.168.43.48/bus/v1/";

    public static final String URL_REGISTER =ROOT_URL+"registration.php";

    public static final String URL_LOGIN =ROOT_URL+"userLogin.php";

    public static final String URL_GETBALANCE = ROOT_URL+"getBalance.php";

    public static final String URL_SETBALANCE = ROOT_URL+"setTransaction.php";

    public static final String URL_CHECK =ROOT_URL+"generateTicket.php";

    public static final String URL_HIST =ROOT_URL+"getHistory.php";

    public static final String URL_BOOK =ROOT_URL+"bookTicket.php";



    public static final String URL_STOP =ROOT_URL+"getStop.php";
    public static final String CONFIRM_URL = "http://192.168.43.48/bus/v1/confirm.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";

    public static final String TAG_RESPONSE= "message";

}
