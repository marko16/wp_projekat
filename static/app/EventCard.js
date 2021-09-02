Vue.component("EventCard", {
    props: {
        event: Object
    },

    created() {
        console.log(event);
        this.event = {
            name: "event1",
            eventType: "tip1",
            description: "Nunc tincidunt volutpat hendrerit. Praesent lacinia arcu in dui imperdiet, ac mollis neque tristique. Maecenas id neque non sem feugiat efficitur. Fusce suscipit turpis nisi, at pretium dui commodo varius. Sed hendrerit et leo a rhoncus. Vivamus lobortis blandit turpis, non aliquet elit dignissim et. Integer quis iaculis ante."
        }
    },

    template:
    `
    <div>
    <div id="container" class="event-container">    
    
    <div class="product-details">
        
    <h1 class="event-title">CHRISTMAS TREE</h1>
    <div class="col rate">
          <input type="radio" id="star52" name="rate" value="5"/>
          <label for="star52" title="text"></label>
          <input type="radio" id="star42" name="rate" value="4"/>
          <label for="star42" title="text"></label>
          <input type="radio" id="star32" name="rate" value="3"/>
          <label for="star32" title="text"></label>
          <input type="radio" id="star22" name="rate" value="2"/>
          <label for="star22" title="text"></label>
          <input type="radio" id="star12" name="rate" value="1"/>
          <label for="star12" title="text"></label> 
    </div>
        
        
    <ul class="information">
        <li><strong>Event type : </strong> {{this.event.eventType}} </li>
        <li><strong>Start : </strong>Olive green</li>
        <li><strong>Average rating : </strong>balls and bells</li>
    </ul>    
<!--    <p class="information">" Let's spread the joy , here is Christmas , the most awaited day of the year.Christmas Tree is what one need the most. Here is the correct tree which will enhance your Christmas.</p>-->

        
    <div class="control">
    
    <button class="btn">
        <span class="price">$250</span>
        <span class="shopping-cart"><i class="fa fa-shopping-cart" aria-hidden="true"></i></span>
        <span class="buy">Get now</span>
    </button>
    
   
    
</div>


       <h5 class="address"><i class="fa fa-map-marker location-pin" aria-hidden="true"></i>XIII Vojvodjanske Brigade 106, Zrenjanin
       </h5>      
</div>
    
<div class="product-image">
    
    <img src="https://citypal.me/wp-content/uploads/2016/04/01-e1462017909580.jpg" alt="">
    

<div class="info">
    
</div>
</div>

</div>
</div>
    `
})