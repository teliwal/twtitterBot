package org.bot.social;

import twitter4j.TwitterException;

public class Main {


    public static void main(String [] args) throws TwitterException {
        RetweetStreamService streamService = new RetweetStreamService();
        streamService.run();
    }


}
