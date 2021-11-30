# ExamineIT-back
Copy of the application ExamineIT

How to create a new device?
To get start with ExamineIT you need to go to DEVICE tab. You will find there a button Add new device. You need to set a title for your device and press Add. After that on the List of devices will appear created device with automatically generated token.

How to add data to your device?

Data can be added only with HTTP protocols. To add data from your device to platform you need to use this url:
https://examine-it-back.herokuapp.com/api/addData/(token)/(data) ,
where token is can be taken from List of devices and example of data looks like "(token)/x=1,y=2,z=3 ". You can add up to 5 variables to one device in ExamineIT. Please notice that if you start with sending one variable, you need to always send one variable to the same device. Similar if you send 5 variables, you always have to send 5 variables. Variables can be described with: [x, y, z, a, b] . It is recommended that after sending first data, put in a comment descrption of it.

How to check data from your device?

Data can be displayed by clicking Check button from List of devices. You can find there users who can access this device, table with corresponding data, as well as chart and comments put by users.

How to add a comment to your device?

To add a comment to your device go to List of device and click on the Check button. At the bottom of a page there is a comment section. Click on a Add a comment button to add a new comment.

How to share your device with other users?

To share your device go to List of devices and check choosed device. Click on a button Share and put a username of the user who you want to grant an access.

How to export data from table?

To share your device go to List of devices and check choosed device. Under the table you will find Export button which allows you to export a data from table to Excel sheet. Export button allows exporting up to 100 rows (choosed inside a table).
.
