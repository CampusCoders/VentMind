# VentMind

  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Android_logo_2019_%28stacked%29.svg/2346px-Android_logo_2019_%28stacked%29.svg.png" width="100"/> <img src="https://firebase.google.com/static/images/brand-guidelines/logo-vertical.png" width="100">
  
  [Our video presentation](https://www.youtube.com/watch?v=G9ZGoM4_3js)
  
  ## Google Technologies Used
- Android SDK
- Google Firebase
    - Firestore Database
    - Authentication
    - Storage

## Other Libraries:
- Glide by [@bumptech](https://github.com/bumptech/glide)

## ♾ Application Features ♾ :
    💖 Share your worries and experiences.
    💖 Share through restricted areas.
    💖 You can comment on posts so you can interact.
    💖 You can earn points for successful comments.
    💖 You can rank higher among other users by increasing the number of posts you send.
    💖 You can see the popularity of restricted domains.
    💖 You can get different avatars with the points you earn.
## About :

Android application developed for GDSC Solution Challenge 2023, where people share their problems and aim to find solutions to them.

### Login and Register Page :

In order to use the application, users must create an account. The user is registered by sending the username, e-mail and password information to firebase. Access can be granted by pulling data from Firebase and querying the user's record.

### Feed Page :

Posts where users tell about their problems are pulled from firebase and presented to all users. If the user wants to reach the posts related to a certain subject, he can filter so that the list is rebuilt by pulling the posts belonging to the subject selected by the user via firebase.

### Experience Page :

Posts where users describe their experiences are pulled from firebase and presented to all users. If the user wants to reach the posts related to a certain subject, he can filter so that the list is rebuilt by pulling the posts belonging to the subject selected by the user via firebase.

### Create Post Page :

Users make entries to create a new post, and by choosing one of the 2 types of posts, their data is added to the list of that type in firebase.

### Comments Page :

Users can interact by commenting on posts. When a new comment entry is received, data is added to firebase. When other users want to reach the comments, the comment data of the selected post from firebase is retrieved.

### Trend Page :

Users choose a topic during post sharing. Each topic is valued according to the number of shares and gains a ranking according to the values of other topics.

### User List Page :

Users can comment on posts. If their comment is selected as valuable by the owner of the post, the user earns points. According to the score, users make certain rankings in the general audience.

### Profile Page :

User-specific data is pulled over firebase and presented to users

### Settings Page :

Users can make changes to their saved data on firebase at any time (username and password reset). They can log out before a new login process, or delete their account if they want, in this case the account information will be deleted via firebase.

## Build Information 🧰 
If you wish to build this project you should make your own firebase project, and use your own firebase json. The project should be on the blaze plan, and have a firestore database, storage and authentication enabled. Other than that it should be built just by opening the project in android studio.

## Screenshots📷
|       **Login Page**               |              **Register Page**     |        **Forgot Password Page**    |            **Feed Page**           |
|:----------------------------------:|:----------------------------------:|:----------------------------------:|:----------------------------------:|
|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screenlogin.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screenregister.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screenforgotpassword.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screenfeed.PNG)|
|         **Create Post Page**      |         **Trend Page**       |           **User List Page**     |           **Profil Page**           |
|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screencreatefeed.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screentrend.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screenuserlist.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screenprofil.PNG)|
|         **Settings Page**      |         **Experience Page**       |           **Comments Page**     |
|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screensettings.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screenexperience.PNG)|![](https://github.com/mudurbeyz/mudurbeyz.github.io/blob/main/Document/screencomments.PNG)|

[Our Video Presentation](https://www.youtube.com/watch?v=LK_cRpyMtHs)

## Authors 📃

- Hasan Ali Çalışkan [Github](https://github.com/hasanalic)
- Okan Çezik [Github](https://github.com/okancezik)
- Emirhan Tarım [Github](https://github.com/mudurbeyz)
- Enes Kadumi [Github](https://github.com/eneskadumi)
