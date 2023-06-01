import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.media.AudioClip;
import java.net.URL;


public class BREAKOUTGAMEfinal extends Application {
    private static final double WIDTH = 1080;
    private static final double HEIGHT = 640;
    private static final double BALL_RADIUS = 13;
    private static final double BALL_SPEED = 10;
    private static final double PADDLE_WIDTH = 1080;
    private static final double PADDLE_HEIGHT = 10;
    private static final double PADDLE_SPEED = 8;
    static final double BRICK_WIDTH = 34; 
    static final double BRICK_HEIGHT = 34;
    private static final double BRICK_GAP = 15;
    private static final int NUM_LIVES=3;
    private Circle ball;
    private Rectangle paddle;
    private Brick[] bricks;
    private double ballSpeedX = BALL_SPEED;
    private double ballSpeedY = BALL_SPEED;
    private double paddleSpeed = 0;
    private Timeline timeline;
    private int numBricksRemaining;
    private int numLives;
    private Text StageText;
    private boolean gamePaused = true;
    private Text pausedText;
    private boolean isMuted = false;
    private AudioClip brickHitSound;
    private AudioClip paddleHitSound;
    private AudioClip pauseonSound;
    private AudioClip pauseoffSound;
   private AudioClip powerupSound;
   private AudioClip level2Sound;
    int currentLevel = 1;
    int score = 0;

//START MESSAGE
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
          String message = "WELCOME \n press space to start and pause \n press esc to exit \n press M to mute sound  " ;   //welcome message with instructions
    Text welcomeText = new Text(message);
    welcomeText.setFont(Font.font("Calibri", FontWeight.BOLD, 70));
    welcomeText.setTextAlignment(TextAlignment.CENTER);
    welcomeText.setFill(Color.YELLOW);
    welcomeText.relocate(WIDTH / 2 - 450, HEIGHT / 2 - 50);       //position of the text
    welcomeText.toFront();

        // Load sound files
        URL brickHitSoundURL = getClass().getResource("block-hit.wav");
        brickHitSound = new AudioClip(brickHitSoundURL.toString());

        URL paddleHitSoundURL = getClass().getResource("block-hit.wav");
        paddleHitSound = new AudioClip(paddleHitSoundURL.toString());
  
        URL pauseonSoundURL = getClass().getResource("pause.mp3");
pauseonSound = new AudioClip(pauseonSoundURL.toString());

                   URL pauseoffSoundURL = getClass().getResource("unpause.mp3");
pauseoffSound = new AudioClip(pauseoffSoundURL.toString());

URL powerupSoundURL = getClass().getResource("new sound.wav");
 powerupSound = new AudioClip(powerupSoundURL.toString());

URL level2SoundURL = getClass().getResource("new sound.wav");
level2Sound = new AudioClip(powerupSoundURL.toString());

        
        // Create the ball
        ball = new Circle(BALL_RADIUS, Color.WHITE);
        ball.relocate(HEIGHT/1.2 - BALL_RADIUS, 590);

        // Create the paddle 
        Rectangle paddleBody = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT);
        paddleBody.setArcWidth(10);
        paddleBody.setArcHeight(10);
        paddleBody.setFill(Color.WHITE);
        paddle = paddleBody;
        paddle.relocate(WIDTH / 2 - PADDLE_WIDTH / 2, HEIGHT - PADDLE_HEIGHT - 8);

     bricks = new Brick[75];     //bricks number of level 1 
        int brickCount = 0;
        Color[] rowColors = {
        Color.DARKORCHID,
        Color.DARKTURQUOISE,
        Color.DARKORCHID,
        Color.DARKTURQUOISE,
        Color.DARKORCHID
};

for (int row = 0; row < 5; row++) {                 //rows number
    Color rowColor = rowColors[row];
    for (int col = 0; col < 15 ; col++) {                     //columns number
        double brickX = col * (BRICK_WIDTH + BRICK_GAP) + BRICK_GAP + 200;      
        double brickY = row * (BRICK_HEIGHT + BRICK_GAP) + BRICK_GAP + 60;
        bricks[brickCount] = new Brick(brickX, brickY, rowColor);
        brickCount++;
    }
}
        numBricksRemaining = 75;
        numLives = NUM_LIVES;

        // Create the score text
        Text scoreText = new Text("Score: 0");
        scoreText.setFont(Font.font("Calibri", FontWeight.BOLD, 35));
        scoreText.setTextAlignment(TextAlignment.CENTER);
        scoreText.relocate(15, 10);
        scoreText.setFill(Color.WHITE);

        // Create the lives text
        Text livesText = new Text("Lives: " + numLives);
        livesText.setFont(Font.font("Calibri", FontWeight.BOLD, 35));
        livesText.setTextAlignment(TextAlignment.CENTER);
        livesText.relocate(WIDTH - 120, 10);
        livesText.setFill(Color.WHITE); 
       
        // current level text
         StageText = new Text("Stage: " + currentLevel);
         StageText.setFont(Font.font("Calibri", FontWeight.BOLD, 35));
         StageText.setTextAlignment(TextAlignment.CENTER);
         StageText.relocate(WIDTH / 2 - 60, 10);
         StageText.setFill(Color.WHITE); 

        // Create the root pane
    Pane root = new Pane();
    root.setPrefSize(WIDTH, HEIGHT);
    root.setStyle("-fx-background-color:BLACK;");
    root.getChildren().addAll(bricks);
    root.getChildren().addAll(ball, paddle, scoreText, livesText, StageText, welcomeText);
         // Create the scene
        Scene scene = new Scene(root);
scene.setOnKeyPressed(e -> {
    if (e.getCode() == KeyCode.LEFT) {
        paddleSpeed = -PADDLE_SPEED;
    } else if (e.getCode() == KeyCode.RIGHT) {
        paddleSpeed = PADDLE_SPEED;
    } else if (e.getCode() == KeyCode.SPACE) {
        if (welcomeText.isVisible()) {
            welcomeText.setVisible(false);
            togglePause();
        } else {
            togglePause();
        }
           } else if (e.getCode() == KeyCode.M) {
        toggleSound();                           // adjust  sound on or off
    } else if (e.getCode() == KeyCode.ESCAPE) {
        primaryStage.close();                  // Exit the game
    }
});

          // right and left button 
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
                paddleSpeed = 0;
            }
        });

    
        
// Create the animation timeline    
         timeline = new Timeline(new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
            double deltaX = ballSpeedX;
            double deltaY = ballSpeedY;
            int score = 0;

@Override
public void handle(ActionEvent event) {
    if (gamePaused) {
        return;
    }

    // Move the paddle
    paddle.setLayoutX(paddle.getLayoutX() + paddleSpeed);

    // Check paddle bounce
    double paddleLeftX = paddle.getLayoutX();
    double paddleRightX = paddle.getLayoutX() + paddle.getWidth();
    if (paddleLeftX < 0) {
        paddle.setLayoutX(0);
    } else if (paddleRightX > WIDTH) {
        paddle.setLayoutX(WIDTH - paddle.getWidth());
    }

    // Move the ball
    ball.setLayoutX(ball.getLayoutX() + deltaX);
    ball.setLayoutY(ball.getLayoutY() + deltaY);

    // Check ball bounce
    double ballLeftX = ball.getLayoutX();
    double ballRightX = ball.getLayoutX() + BALL_RADIUS * 2;
    double ballTopY = ball.getLayoutY();
    double ballBottomY = ball.getLayoutY() + BALL_RADIUS * 2;

    if (ballLeftX <= 0 || ballRightX >= WIDTH) {
        deltaX *= -1; // Change ball direction
    }

    if (ballTopY <= 0) {
        deltaY *= -1; // Change ball direction
    } 

    if (ballBottomY >= HEIGHT) {
        numLives--;
        gamePaused = true;
        timeline.pause();
        livesText.setText("Lives: " + numLives);
        if (numLives <= 0) {
            timeline.stop();
            showGameOver(primaryStage, score, false);
        } else {
            resetBallAndPaddle();
        }
    }

   // Check collision with paddle
                if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
                    deltaY *= -1; // Change ball direction
                    paddleHitSound.play(); // Play paddle hit sound
                }
    // Check collision with bricks
                boolean allBricksDestroyed = true;
                for (int i = 0; i < bricks.length; i++) {
                    Brick brick = bricks[i];
                    if (!brick.isDestroyed() && ball.getBoundsInParent().intersects(brick.getBoundsInParent())) {
                        brick.setDestroyed(true);
                        root.getChildren().remove(brick);  //remove the hit brick
                        // scoere increasing
                        numBricksRemaining--;
                        score += 10;
                        scoreText.setText("Score: " + score);

                        if (numBricksRemaining == 0) {
                            if (currentLevel < 2) {
                                gamePaused = true;
                                timeline.pause();
                                timeline.stop();
                                showNextLevel(primaryStage, score);
                            } else {
                                timeline.stop();
                                showGameOver(primaryStage, score, true);
                            }
                        }

                        if (ballLeftX + BALL_RADIUS <= brick.getLayoutX() || ballRightX - BALL_RADIUS >= brick.getLayoutX() + BRICK_WIDTH) {
                            deltaX *= 1; // Change ball direction
                        } else {
                            deltaY *= 1; // Change ball direction
                        }

                        brickHitSound.play(); // Play brick hit sound
                    }

                    if (!brick.isDestroyed()) {
                        allBricksDestroyed = false;
                    }
                }

    if (allBricksDestroyed) {
        timeline.stop();
        showGameOver(primaryStage, score, true);
    }

 // Check score for paddle width adjustment and powerup
if (score >= 200 && score <= 500) {
    if (paddle.getWidth() != BREAKOUTGAMEfinal.PADDLE_WIDTH * 2 && ball.getRadius()!= BREAKOUTGAMEfinal.BALL_RADIUS *2 ) {
        paddle.setWidth(BREAKOUTGAMEfinal.PADDLE_WIDTH * 2 );
        ball.setRadius(BREAKOUTGAMEfinal.BALL_RADIUS *2);
        powerupSound.play(); // Play new sound
        Text powerupText = new Text("Powerup Activated");
        powerupText.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
        powerupText.setFill(Color.RED);
        powerupText.relocate(WIDTH / 2 - 510, HEIGHT / 1.12); // Position of the powerup text
        root.getChildren().add(powerupText);
    }
} else {
    if (paddle.getWidth() != BREAKOUTGAMEfinal.PADDLE_WIDTH && ball.getRadius()!= BREAKOUTGAMEfinal.BALL_RADIUS) {
        paddle.setWidth(BREAKOUTGAMEfinal.PADDLE_WIDTH);
         ball.setRadius(BREAKOUTGAMEfinal.BALL_RADIUS );
        root.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().equals("Powerup Activated"));
    }
}
}
        }));
                      timeline.setCycleCount(Timeline.INDEFINITE);

        // Show the stage
        primaryStage.setTitle("Breakout Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Start the game
        timeline.play();
    }

   private void togglePause() {
    if (gamePaused) {
        gamePaused = false;
        timeline.play();
        pauseonSound.play();
    } else {
        gamePaused = true;
        timeline.pause();
        pauseoffSound.play();
    }
}
   private void toggleSound() {
    isMuted = !isMuted;
    if (isMuted) {
        // Mute the sound
        brickHitSound.setVolume(0);
        paddleHitSound.setVolume(0);
        pauseonSound.setVolume(0);
        pauseoffSound.setVolume(0);
        powerupSound.setVolume(0);
    } else {
        // play the sound
        brickHitSound.setVolume(1.0);
        paddleHitSound.setVolume(1.0);
        pauseonSound.setVolume(1.0);
        pauseoffSound.setVolume(1.0);
        powerupSound.setVolume(1.0);
    }
}
   
private void resetBallAndPaddle() {
  
               ball.relocate(HEIGHT/1.2 - BALL_RADIUS, 590);
               paddle.setLayoutX(WIDTH / 2 - PADDLE_WIDTH / 2);
               paddle.setLayoutY(HEIGHT - PADDLE_HEIGHT - 8);
               ballSpeedX = BALL_SPEED;
               ballSpeedY = BALL_SPEED;
}

   private void showGameOver(Stage primaryStage, int score, boolean victory) {
    String message = victory ? "Congratulations!\nYou won!\nPress Space To Restart\nScore: " + score : "Game Over!\nPress Space To Restart\nScore: " + score;     
    Text gameOverText = new Text(message);
    gameOverText.setFont(Font.font("Calibri", FontWeight.BOLD, 50));
    gameOverText.setTextAlignment(TextAlignment.CENTER);
    gameOverText.setFill(Color.RED);
  gameOverText.relocate(WIDTH / 2 - 220, HEIGHT / 2 - 50); 
    Pane root = (Pane) primaryStage.getScene().getRoot();
    root.getChildren().add(gameOverText);
    
    primaryStage.getScene().setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.SPACE) {
            restartGame(primaryStage);
        }
    });
}

private void restartGame(Stage primaryStage) {
    Pane root = (Pane) primaryStage.getScene().getRoot();
    root.getChildren().clear();                  // Clear all existing 

    // Reset game state
    numLives = NUM_LIVES;
    currentLevel = 1;

    // Create and start a new game
    start(primaryStage);
}

private void showNextLevel(Stage primaryStage, int score) {       //moving to level 2
    currentLevel++;
    String message = "Level " + currentLevel + "\nScore: " + score;
    Text nextLevelText = new Text(message);
    nextLevelText.setFont(Font.font("Calibri", FontWeight.BOLD, 70));
    nextLevelText.setTextAlignment(TextAlignment.CENTER);
    nextLevelText.setFill(Color.RED);
    nextLevelText.relocate(WIDTH / 2 - 150, HEIGHT / 2 - 40);          //position of the text
      level2Sound.play(); // Play level2 sound
    Pane root = (Pane) primaryStage.getScene().getRoot();
    root.getChildren().add(nextLevelText);

    resetBallAndPaddle();
    numLives = NUM_LIVES;
    if (currentLevel == 2) {
        bricks = new Brick[110];           //bricks number of level 2
        int brickCount = 0;
        Color[] rowColors = {              //bricks colors
                Color.GOLD,
                Color.DARKTURQUOISE,
                Color.MEDIUMPURPLE,
                Color.DARKTURQUOISE,
                Color.GOLD,
        };

        for (int row = 0; row < 5; row++) {                 //level 2 rows number
            Color rowColor = rowColors[row];
            for (int col = 0; col < 22; col++) {                 //level 2 columns number
                double brickX = col * (BRICK_WIDTH + BRICK_GAP) + BRICK_GAP + 0;
                double brickY = row * (BRICK_HEIGHT + BRICK_GAP) + BRICK_GAP + 60;
                bricks[brickCount] = new Brick(brickX, brickY, rowColor);
                brickCount++;
            }
        }
        numBricksRemaining = 115;
    }

// Create the scene and paddle control
    primaryStage.getScene().setOnKeyPressed(null);
    primaryStage.getScene().setOnKeyReleased(null);
    primaryStage.getScene().setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.LEFT) {
            paddleSpeed = -PADDLE_SPEED;
        } else if (e.getCode() == KeyCode.RIGHT) {
            paddleSpeed = PADDLE_SPEED;
        } else if (e.getCode() == KeyCode.SPACE) {
            togglePause();
                  } else if (e.getCode() == KeyCode.M) {
        toggleSound();                           // adjust  sound on or off
    } else if (e.getCode() == KeyCode.ESCAPE) {
        primaryStage.close();                  // Exit the game
    }
   
    });
    
    primaryStage.getScene().setOnKeyReleased(e -> {
        if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
            paddleSpeed = 0;
        }
    });

    // Remove existing bricks
    primaryStage.getScene().getRoot().getChildrenUnmodifiable().stream()
            .filter(node -> node instanceof Brick)
            .forEach(node -> ((Pane) primaryStage.getScene().getRoot()).getChildren().remove(node));
    ((Pane) primaryStage.getScene().getRoot()).getChildren().addAll(bricks);

       // Update the StageText with the current level value
    StageText.setText("Stage: " + currentLevel);

    // Bring the text to the front
    nextLevelText.toFront();
    
    // Remove the nextLevelText after 2 seconds
    Timeline removeTextTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
        root.getChildren().remove(nextLevelText);
    }));
  
    removeTextTimeline.play();
    timeline.play();
}
private static class welcomeText {
    }
    private static class powerupSound {

        private static void play() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        private static void setVolume(int i) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        private static void setVolume(double d) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public powerupSound() {
        }
    }
}
final class Brick extends Rectangle {
    private boolean destroyed;

    public Brick(double x, double y, Color color) {
        super(BREAKOUTGAMEfinal.BRICK_WIDTH, BREAKOUTGAMEfinal.BRICK_HEIGHT, Color.TRANSPARENT);
        relocate(x, y);
        setFill(color);
        setDestroyed(false);
    }
    public boolean isDestroyed() {
        return destroyed;
    }
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
