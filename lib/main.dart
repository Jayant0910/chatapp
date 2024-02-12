import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:url_launcher/url_launcher.dart';
import 'chat.dart';

void main (){

  runApp(MaterialApp(
    debugShowCheckedModeBanner: false,
    theme: ThemeData(
      primarySwatch: Colors.blue,
    ),
   home: chatapp(),
  ));
}
class chatapp extends StatefulWidget {
  const chatapp({super.key});

  @override
  State<chatapp> createState() => _chatappState();
}

void _launcherURL(int value)async{
   String url="";
   if(value ==1){
     url="https://auth.geeksforgeeks.org/roadBlock_v2.php/";
   }

   if(await canLaunch(url)){
    await launch(url,
      forceSafariVC: true,
      enableJavaScript: true,
    );
   }else{
     throw "could not Launch $url";
   }
}
class _chatappState extends State<chatapp> {
  final List<String> imageList = [

    "https://m.media-amazon.com/images/I/71RTS7cqWaL._UF1000,1000_QL80_.jpg"
  ];
  //get imageList => null;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:  AppBar(
        title: Text("Customer Assistant"),
        centerTitle: true,
        actions: [
          Padding(padding: EdgeInsets.only(right: 20.0),
          child: GestureDetector(
            onTap: (){
              Navigator.push(context, MaterialPageRoute(builder: (context)=>Chat()));
            },
            child: Icon(
              Icons.help,
              size: 25.0,
            ),
          ),
          )
        ],
      ),
      drawer: new Drawer(
        child: new ListView(
          children: [
            new UserAccountsDrawerHeader(accountName: Text("DTU"),
                accountEmail: Text("jayantdtuee@gmail.com"),
              currentAccountPicture: new CircleAvatar(
               child: Image.network('https://picsum.photos/250?image=9'),
              ),
            ),
            new ListTile(
              title: new Text("website!"),
              trailing:new Icon(Icons.web) ,
             onTap: (){
               _launcherURL(1);
              },
            ),
          ],
        ),
      ),
      body: ListView(
        padding: EdgeInsets.only(top: 40.0),
        children: [
          Center(
            child: CarouselSlider(
              options: CarouselOptions(
                enlargeCenterPage: true,
                enableInfiniteScroll: true,
                autoPlay: true
              ),
              items: imageList.map((e) => ClipRRect(
                borderRadius: BorderRadius.circular(10.0) ,
                child: Stack(
                  fit: StackFit.expand,
                  children: [
                    Image.network(e,
                    width: 1050.0,
                    height: 350.0,
                    fit: BoxFit.cover,
                    )
                  ],


                ) ,
              )).toList(),
            ),
          )

             ],

             ),
             floatingActionButton: FloatingActionButton.extended(
               icon: Icon(Icons.chat),
               label: Text("Chat Bot"),
               tooltip: 'Connect to Assistant',
               onPressed: (){
                 Navigator.push(context, MaterialPageRoute(builder: (context)=>Chat()));
               },
             ),

           );


 }
}
