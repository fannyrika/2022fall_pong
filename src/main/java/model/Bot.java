package model;
import java.util.Random;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Bot extends Court{

    double bot;
    private int difficulty;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    public Bot(RacketController playerA,int difficulty,double acceleration){
      super(playerA,null,acceleration,null,null);
      this.difficulty=difficulty;
    }

    public void setDifficulty(int n) {
    	difficulty=n;
    }

    public double getBot(){
      return bot;
    }
    public void setBot(double x){
      bot=x;
    }
    public void setScoreP1(int s){
        scoreP1=s;
     }
     public void setScoreP2(int s){
        scoreP2=s;
     }
     public int getScoreP1(){
        return scoreP1;
     }
     public int getScoreP2(){
        return scoreP2;
     }


    public void updateWithBot(double deltaT) {

        switch (this.getPlayerA().getState()) {
            case GOING_UP:
                this.setRacketA(this.getRacketA()-this.getRacketSpeed() * deltaT);
                if (this.getRacketA() < 0.0) this.setRacketA(0.0);
                break;
            case IDLE:
                break;
            case GOING_DOWN:
            this.setRacketA(this.getRacketA()+this.getRacketSpeed() * deltaT);

                if (this.getRacketA() + this.getRacketSize() > this.getHeight())   this.setRacketA(this.getHeight()-this.getRacketSize());
                break;
        }
        deplacementBot(deltaT);

        if (updateBallWithBot(deltaT)) reset();
    }
    /**
     * Fonction qui gére le déplacement du bot
     * En fonction de la difficultée le bot rate ou non plus facilement des balles;
     * @param deltaT
     */
    public void deplacementBot(double deltaT) {
    	//le bot rate une balle sur 10;
    	 if(difficulty==0){
             if(this.getBallSpeedX()>0&&this.getBallX()>getWidth()/3){
               Random x= new Random();
               int alea=x.nextInt(10);
               if(alea>=1){

                   if(getBallSpeedY()<0) bot -= Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
          	 	   if(getBallSpeedY()>0)bot+=Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
                   if (bot< 0.0) bot = 0.0;
                   if (bot + this.getRacketSize() > this.getHeight()) bot = this.getHeight() - this.getRacketSize();
               }
             }
             //deplace le bot de manière aleatoire lorsque la vitesse de la balle est negative ou bien lorque la balle depasse les 1/3 du terrain;
             else{
  		       deplacementAleatoire(deltaT);
  		     }
    	 }
    	//le bot rate une balle sur 15;
         if(difficulty==1){
         	Random x= new Random();
             int alea=x.nextInt(15);
             if(this.getBallSpeedX()>0&&this.getBallX()>getWidth()/3){
               if(alea>=1){
            	 if(getBallSpeedY()<0) bot -= Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
        	 	 if(getBallSpeedY()>0)bot+=Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
                 if (bot< 0.0) bot = 0.0;
                 if (bot + this.getRacketSize() > this.getHeight()) bot = this.getHeight() - this.getRacketSize();
               }
             }
             else {
	        	   deplacementAleatoire(deltaT);
	           }
         }
       //le bot rate une balle sur 20;
         if(this.difficulty==2){
     	 	if(this.getBallSpeedX()>0) {
     	 		Random x= new Random();
                int alea=x.nextInt(20);
                if(alea>=1) {
	              if(getBallSpeedY()<0) bot -= Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
	   	 		  if(getBallSpeedY()>0)bot+=Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
	              if (bot< 0.0) bot = 0.0;
	              if (bot + this.getRacketSize() > this.getHeight()) bot = this.getHeight() - this.getRacketSize();
                }
     	 	}
     	 	else{
		       deplacementAleatoire(deltaT);
		    }

         }
         //le bot ne rate aucune balle;
         if(this.difficulty==3){
 	 	if(this.getBallSpeedX()>0) {
 	 		if(getBallSpeedY()<0) bot -= Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
 	 		if(getBallSpeedY()>0)bot+=Math.abs(getBallSpeedY())*deltaT-getRacketSize()/2*deltaT;
          if (bot< 0.0) bot = 0.0;
          if (bot + this.getRacketSize() > this.getHeight()) bot = this.getHeight() - this.getRacketSize();
 	 	}

	       else {
	    	   deplacementAleatoire(deltaT);
	        }

  	}
    }
    public void deplacementAleatoire(double deltaT) {

    	if(getBallSpeedY()>0&&getRacketA()>getHeight()/2) {
    		bot-=getRacketA()*deltaT;
    		if (bot< 0.0) bot = 0.0;
    	}
    	if(getBallSpeedY()<0&&getRacketA()<getHeight()/2) {
    		bot+=getRacketA()*deltaT;
    		if (bot + this.getRacketSize() > this.getHeight()) bot = this.getHeight() - this.getRacketSize();
    	}
    	else {
    		bot+=getRacketSize()*deltaT;
    		if (bot< 0.0) bot = 0.0;
    		if (bot + this.getRacketSize() > this.getHeight()) bot = this.getHeight() - this.getRacketSize();
    	}
    }


    private boolean updateBallWithBot(double deltaT){
      double nextBallX = this.getBallX() + deltaT * this.getBallSpeedX();
      double nextBallY = this.getBallY() + deltaT * this.getBallSpeedY();
      // next, see if the ball would meet some obstacle
      if (nextBallY < 0 || nextBallY > this.getHeight()) {
          this.setBallSpeedY(this.getBallSpeedY()-2.0*this.getBallSpeedY());
          nextBallY = this.getBallY() + deltaT * this.getBallSpeedY();
      }
      if ((nextBallX < 0 && nextBallY > this.getRacketA() && nextBallY < this.getRacketA() + this.getRacketSize())
              || (nextBallX > this.getWidth()-100 && nextBallY > bot && nextBallY < bot + this.getRacketSize())) {
                MediaPlayer mP = new MediaPlayer(media);
                MediaView mV = new MediaView(mP);
                mP.setAutoPlay(true);
                mP.setVolume(10);
          this.setBallSpeedX(this.getBallSpeedX()-2*this.getBallSpeedX());
          nextBallX = this.getBallX() + deltaT * this.getBallSpeedX();
      } else if (nextBallX < 0) {
        MediaPlayer mP2 = new MediaPlayer(media2);
        MediaView mV2 = new MediaView(mP2);
        mP2.setVolume(0.1);
          mP2.setAutoPlay(true);
      	  this.scoreP2++;
          return true;
      } else if (nextBallX > this.getWidth()-100) {
        MediaPlayer mP2 = new MediaPlayer(media2);
        MediaView mV2 = new MediaView(mP2);
        mP2.setVolume(0.1);
          mP2.setAutoPlay(true);
      	  this.scoreP1++;
          return true;
      }
      this.setBallx(nextBallX);
      this.setBallY(nextBallY);
      return false;
    }

	public void resetScore(){
		scoreP1 = 0;
   		scoreP2 = 0;
	}
	public boolean player1Win() {
	    	return scoreP1>scoreP2;
	    }
	public boolean player2Win() {
	    	return scoreP1<scoreP2;
	    }
	public boolean scoreEgalite() {
	    	return scoreP1==scoreP2;
	    }
	  public boolean scoreLimitReach() {
	    	return scoreP1==score||scoreP2==score;
	    }
    public void reset() {
        this.setRacketA(this.getHeight()/2);
        this.bot=this.getHeight()/2;
        this.setRacketSpeed(300.00);
        this.setBallSpeedX(400.0);
        this.setBallSpeedY(400.0);
        this.setBallx(this.getWidth()/2);
        this.setBallY(this.getHeight()/2);


    }

}
