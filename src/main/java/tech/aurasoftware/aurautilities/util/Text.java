package tech.aurasoftware.aurautilities.util;


import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class Text {

    public static String convertMillis(long millis){
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        long secondsLeft = seconds % 60;
        long minutesLeft = minutes % 60;
        long hoursLeft = hours % 24;
        long daysLeft = days % 7;
        long weeks = days / 7;

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(append("Week", weeks));
        stringBuilder.append(append("Day", daysLeft));
        stringBuilder.append(append("Hour", hoursLeft));
        stringBuilder.append(append("Minute", minutesLeft));
        stringBuilder.append(append("Second", secondsLeft));

        return stringBuilder.toString();
    }

    private String append(String type, long value){
        if(value == 0){
            return "";
        }

        String message = value + " " + type;

        if(value > 1){
            message += "s ";
        }else{
            message += " ";
        }

        return message;

    }

    public static String c(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
