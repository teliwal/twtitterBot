package org.bot.social;

import twitter4j.*;

/**
 * Classe te retweet "a la demande" , on recupere les tweets qui nous interessent et on retweet
 */
public class RetweetService {


    private String url = "https://twitter.com/";
    private String monNom = "andi_zola";

    public void retweet() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();

        Query query = new Query("#AllRising4Guinea");
        query.setCount(100);
        QueryResult result = twitter.search(query);

        System.out.println(result.getTweets().size());
        for (Status status : result.getTweets()) {
            if(!monNom.equals(status.getUser().getScreenName())) {
                retweet(twitter, status);
            }
        }
    }

    private void retweet(Twitter twitter,Status status){
        try {
            String newStatus = "#AllRising4Guinea\n" + url + status.getUser().getScreenName()+ "/status/" + (status.getId());
            twitter.updateStatus(newStatus);
            System.out.println(newStatus);
        } catch (TwitterException e){
            System.out.println("echec de retweet");
        }
    }
}
