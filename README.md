# BREAKOUT-GAME-javafx
brick breaker / wall breaker / breakout game in 2d using javafx
The code you provided is an implementation of a simple Breakout game using JavaFX. It creates a game window where the player controls a paddle to bounce a ball and destroy bricks. Here's a breakdown of the code:

The code starts by importing the necessary JavaFX classes and other dependencies.

The BREAKOUTGAMEfinal class extends Application and overrides the start method, which is the entry point of the JavaFX application.

The code defines various constants such as window dimensions, ball properties, paddle properties, brick properties, and game settings.

Inside the start method, the UI elements such as the ball, paddle, bricks, score, lives, and welcome text are created and positioned using JavaFX's layout system.

The Scene and Stage are created to display the game window, and event handlers are registered to handle keyboard input.

The animation timeline is created using a Timeline object. Inside the timeline's event handler, the game logic is implemented, including paddle movement, ball movement, collision detection with walls, paddle, and bricks.

The togglePause method allows the player to pause and resume the game by pressing the spacebar.

The resetBallAndPaddle method is called when the player loses a life and resets the position and speed of the ball and paddle.

The showGameOver method displays the game over screen when the player loses or wins the game. It shows the score and allows the player to restart the game by pressing the spacebar.

The restartGame method is called to restart the game when the player chooses to play again after game over.

The showNextLevel method is called when the player completes a level. It displays the next level screen and updates the brick layout for the next level.

Finally, the main method launches the JavaFX application.

To run this code, you need to have the JavaFX libraries set up correctly in your development environment. You can refer to the official documentation or online resources for instructions on setting up JavaFX.
![6](https://github.com/YasseenLMaghraby/BREAKOUT-GAME-javafx/assets/132460123/6e46970f-f20f-4773-8c02-58c137d8a8df)
![Untitled](https://github.com/YasseenLMaghraby/BREAKOUT-GAME-javafx/assets/132460123/03d185af-c978-42c7-86fa-10bbcb9e4885)
![2](https://github.com/YasseenLMaghraby/BREAKOUT-GAME-javafx/assets/132460123/e97aa535-b7c6-42b5-961e-5708f394a3bf)
![3](https://github.com/YasseenLMaghraby/BREAKOUT-GAME-javafx/assets/132460123/468e7911-b915-4675-a303-6a375354ce9c)
![4](https://github.com/YasseenLMaghraby/BREAKOUT-GAME-javafx/assets/132460123/e955b521-90b8-41df-8486-ab723b2a3c6e)
![5](https://github.com/YasseenLMaghraby/BREAKOUT-GAME-javafx/assets/132460123/25ce7cf6-5744-40a6-ac6a-7040e1520b20)
