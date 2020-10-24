package org.bot.social;

import twitter4j.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe de retweet en continu. on
 */
public class RetweetStreamService {

    private String twitterUrl = "https://twitter.com/";
    private String twitterName = "andi_zola"; // a remplacer par mon votre nom tweeter
    private int count = 0;
    private int maxTweets = 97;
    private String hashTagToAdd = "#AllRising4Guinea\n";

    String keywords[] = {"#AllRising4Guinea","#GuineeVote2020", "#CENI", "Alpha Condé"};

    Twitter twitter = TwitterFactory.getSingleton();

    Set<Long> retweetedStatus = new HashSet<>();

    public void run(){
        TwitterStream twitterStream = TwitterStreamFactory.getSingleton();

        StatusListener statusListener = createListener();

        FilterQuery fq = new FilterQuery();

        //on enregistre les mot clés qu'on surveille
        fq.track(keywords);

        twitterStream.addListener(statusListener);
        //on commence a suivre les tweets qui nous interessent
        twitterStream.filter(fq);
    }

    private StatusListener createListener(){
        return  new StatusListener() {
            // a chaque fois qu'on voit un tweet qui correspond à nos mot clés
            public void onStatus(Status status) {
                //on ne retweet pas mes propres tweets
                if(twitterName.equals(status.getUser().getScreenName())) {
                    return;
                }
                //si le tweet est deja dans les tweets retweetes on l'ignore
                if(retweetedStatus.contains(status.getId())){
                    return;
                }

                //il s'agit d'un retweet, on peut decider de retweet uniquement des tweets frais, sinon commenter
                // ce check
                if(status.getQuotedStatus() != null){
                    return;
                }
                //on retweete uniquement si on a pas atteint le max
                if(retweetedStatus.size() < maxTweets) {
                    retweet(status);
                } else {
                    System.out.println("Nombre maximal de tweet atteint attendre 1heure");
                    System.out.println("Tweet is " + status.getText());
                }
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { }

            public void onTrackLimitationNotice(int i) { }

            public void onScrubGeo(long l, long l1) { }

            public void onStallWarning(StallWarning stallWarning) { }

            public void onException(Exception e) {  }
        };
    }

    private void retweet(Status status) {
        try {
            String newStatus = hashTagToAdd + twitterUrl + status.getUser().getScreenName() + "/status/" + (status.getId());
            twitter.updateStatus(newStatus);
            retweetedStatus.add(status.getId());
            System.out.println("Retweeted "+status.getText());
        } catch (Exception e) {
            System.out.println("echec de retweet" + e.getMessage());
        }
    }
}
