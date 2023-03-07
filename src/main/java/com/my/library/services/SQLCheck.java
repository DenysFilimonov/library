package com.my.library.services;

import com.my.library.db.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLCheck {

    private static SQLCheck instance;
    private static final Object mutex = new Object();

    public static SQLCheck getInstance(){
        SQLCheck result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new SQLCheck();
            }
        }
        return result;
    }

    public boolean isInjectionPresent(String requestParameter){

        final String regexTextBlock = "('(''|[^'])*')|(')?";
        final Pattern patternTextBlock = Pattern.compile(regexTextBlock, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcherTextBlock = patternTextBlock.matcher(requestParameter);
        boolean isTextBlockPresent = matcherTextBlock.find();

        final String regex = "(;)|(\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(requestParameter);
        if (matcher.find()&& isTextBlockPresent) return true;

        final String regexSleep = "(sleep)+(\\(.+\\))|(or)+.*(\\=)";
        final Pattern patternSleep = Pattern.compile(regexSleep, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcherSleep = patternSleep.matcher(requestParameter);
        if (matcherSleep.find()&&isTextBlockPresent) return true;

        final String regexComment = "/[\\t\\r\\n]|(--[^\\r\\n]*)|(\\/\\*[\\w\\W]*?(?=\\*)\\*\\/)";
        final Pattern patternComment = Pattern.compile(regexComment, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcherComment = patternComment.matcher(requestParameter);
        if (matcherComment.find()) return true;
        return false;
    }

    public static void main(String[] args){
        System.out.println(new SQLCheck().isInjectionPresent("seLect FRom--"));
    }

}
