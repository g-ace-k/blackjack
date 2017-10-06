# Blackjack
App can be found on the Google Play Store
https://play.google.com/store/apps/details?id=com.mikegacek.blackjack

Current code on github is the code for the soon to be released verion 2.0 that introduces a larger screen resolution for more clear and crisp graphics, revamped settings, statistics, and menu as well as several bug fixes</br>
To see the current code of the version on the playstore, please see the initial commit found here.</br>

The app starts up at the Blackjack class, and from there loads all Texture Assets and creates the Main screen page.
The main screen page is responisble for keeping track of what state the game is in, as well as all touch screen presses, all calls to the update and render functions from the managing classes. The Game manager is responisble for running and updating all things of the game blackjack such as the deck, the strategy tables, the side bet games and the actions of dealer and player. The Chip Manager is responible for keeping track of all the chips and money in play. The settings manager keeps track of which settings are currently enabled. The Statistics Manager keeps track of all the stats. The Game Renderer draws out all the Textures onto the screen given the state of the game atm. The framework handles the backend of each of these classes and allows a smooth experience and an app that doesnt take up a lot of storage.

https://github.com/g-ace-k/Blackjack/tree/1a8b0cc09cddac467474061d64e248eec7e767c7/app/src/main/java/com/mikegacek/blackjack
