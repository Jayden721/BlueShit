package com.blueshit.utils;

import android.content.Context;

import com.blueshit.R;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author sunfuyong
 *
 */
public class StringUtil {
	
	
	/**
	 * 判断字符串是否为手机号码
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumber(String str){
		if(str == null){
			return false;
		}
		return str.matches("^(13|15|18|17|14)[0-9]{9}$");
	}

	/**
	 * 判断字符串去除两端空格后是否为空串
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if(str == null||str.equals("")||str.trim().equals("")){
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(String str){
		if(str == null||str.equals("")){
			return true;
		}
		return false;
	}
	
	/**
	 * 替换字符串中的换行符
	 * @param src
	 * @return
	 */
	public static String insteadChangeLine(String src){
		if(isEmpty(src)){
			return null;
		}else{
			return src.replace("\r\n", "\n");
		}
	}
	
    /**
     * 待验证的字符串
     * @return 如果是符合邮箱格式的字符串,返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isEmail( String email ) {
    	 String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
         Pattern p = Pattern.compile(str);     
         Matcher m = p.matcher(email);     
         return m.matches();     
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    //从sdk中提到了ui中，使用更简单不犯错的获取string方法
//              digest = EasyUtils.getAppResourceString(context, "location_recv");
                    digest = getStrng(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
//              digest = EasyUtils.getAppResourceString(context, "location_prefix");
                    digest = getStrng(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                digest = getStrng(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getStrng(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getStrng(context, R.string.video);
                break;
            case TXT: // 文本消息
                if(!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL,false)){
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                }else{
                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
                    digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
                }
                break;
            case FILE: //普通文件消息
                digest = getStrng(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    static String getStrng(Context context, int resId){
        return context.getResources().getString(resId);
    }

}
