package com.hudson.thinksns.statusparse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
/**
 * 
 * @author 石仲才
 * 
 */
public class WeiBoPrase {
	 private final static Pattern AT_PATTERN = Pattern.compile("@[\u4e00-\u9fa5\\w\\-]+");
	 private final static Pattern TAG_PATTERN =Pattern.compile("#[\u4e00-\u9fa5\\w\\-]+#");
	 private final static Pattern URL_PATTERN =Pattern.compile("http://t.cn/\\w+");
	 private final static Pattern EMOTION_PATTERN=Pattern.compile("\\[.*?\\]");
  public static SpannableString parseContent(String content,Context act){
	  SpannableString spannableString = new SpannableString(content);  
		Matcher AT_Matcher=AT_PATTERN.matcher(content);
		while(AT_Matcher.find()){
			String str=AT_Matcher.group();//获取到@XX
			
			spannableString.setSpan(new WeiboAtSpan(str.substring(1,str.length()),act),
					AT_Matcher.start(), AT_Matcher.end(), 
					SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		}
		Matcher Tag_Matcher=TAG_PATTERN.matcher(content);
		while(Tag_Matcher.find()){
			String str=Tag_Matcher.group();//获取到话题
			spannableString.setSpan(new WeiboTagSpan(str.substring(1,str.length()-1),act),
					Tag_Matcher.start(), Tag_Matcher.end(), 
					SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		}
		Matcher Url_Matcher=URL_PATTERN.matcher(content);
		while(Url_Matcher.find()){
			String str=Url_Matcher.group();//获取到URL
			spannableString.setSpan(new WeiboUrlSpan(str,act),
					Url_Matcher.start(), Url_Matcher.end(), 
					SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		}
		Matcher Emotion_Matcher=EMOTION_PATTERN.matcher(content);
		int end=0;
		while(Emotion_Matcher.find()){
			String str=Emotion_Matcher.group();//[表情]
			if(EmotionMap.getEmMapS().containsKey(str)){
	      		Drawable drawable2=act.getResources().getDrawable(EmotionMap.getEmMapS().get(str));  
	            drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth()*2, drawable2.getIntrinsicHeight()*2);  
	            ImageSpan imageSpan=new ImageSpan(drawable2,ImageSpan.ALIGN_BASELINE);
	    	    spannableString.setSpan(imageSpan, end+content.indexOf(str), end+content.indexOf(str)+str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	      
	      	}

	  	   end+=content.indexOf(str)+str.length();
	      	content=content.substring(content.indexOf(str)+str.length(), content.length());

	      	Emotion_Matcher=EMOTION_PATTERN.matcher(content);
			
		}
		return spannableString;
		
  }
}
