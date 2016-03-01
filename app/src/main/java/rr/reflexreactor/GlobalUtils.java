package rr.reflexreactor;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shweta on 2/8/2016.
 */
public class GlobalUtils {
    public static String getRegistrationId(Context context)
    {
        SharedPreferences sharedPref =context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.saved_reg_id),"");
    }
    public static String getPhnNumber(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.saved_phn),"");
    }
    public static String url_online = "http://192.168.43.98/show_online.php";
    public  static String url_send_game_request="http://192.168.43.98/send_game_request.php";
    public  static  String url_get_all_requests="http://192.168.43.98/get_all_requests.php";
    public static  String url_show_request="http://192.168.43.98/show_request.php";
    public static  String url_check_valid_phn="http://192.168.43.98/check_valid_phn.php";
    public  static String PROJECT_NUMBER="546205713647";
    public static  String url_update_reg_id="http://192.168.43.98/update_reg_id.php";
    public static String url_accept_request = "http://192.168.43.98/accept_request2.php";
    public static String url_getAcceptedUsers="http://192.168.43.98/getAcceptedUsers.php";
    public static String url_start_game="http://192.168.43.98/start_game.php";
    public static String url_update_result="http://192.168.43.98/update_result.php";
    public static String url_check_all_updated="http://192.168.43.98/check_all_updated.php";
    public static String url_fetch_result="http://192.168.43.98/fetch_results.php";
    public static String url_check_if_game_valid="http://192.168.43.98/check_if_game_valid.php";
    public static String url_update_rating="http://192.168.43.98/update_rating.php";
    public static String url_delete_game_request="http://192.168.43.98/delete_game_request.php";
    public static String url_decline_request="http://192.168.43.98/decline_request.php";


}
