function generateTile(heading, description, id){
	const tileContainer = document.getElementById("tileContainer");
	const tileImageContainer = document.getElementById("tileImageContainer");
	
	const newDivHeadingText = document.createElement("h2");
	newDivHeadingText.classList.add("tileHeadingText");
	newDivHeadingText.innerHTML = heading;
	
	const newDivHeading = document.createElement("div");
	newDivHeading.classList.add("tileHeading");
	newDivHeading.appendChild(newDivHeadingText);
	
	const newDivDescriptionText = document.createElement("p");
	newDivDescriptionText.classList.add("tileDescriptionText");
	newDivDescriptionText.innerHTML = description;
		
	const newDivDescription = document.createElement("div");
	newDivDescription.classList.add("tileDescription");
	newDivDescription.appendChild(newDivDescriptionText);
	
	const newDivGraphic = document.createElement("div");
	newDivGraphic.classList.add("tile");
	newDivGraphic.appendChild(newDivHeading);
	newDivGraphic.appendChild(newDivDescription);
		
	
	const newDiv = document.createElement("div");
	newDiv.classList.add("tileHidden");
	newDiv.appendChild(newDivGraphic);
	newDiv.classList.add("tile" + id);
	tileContainer.appendChild(newDiv);
	const newImageDiv = document.createElement("div");
	const newImageDivGraphic = document.createElement("div");
	newImageDiv.classList.add("tileHidden");
	newImageDivGraphic.classList.add("tile");
	newImageDivGraphic.innerHTML = "<img src='../images/ducks.png'>";
	newImageDiv.appendChild(newImageDivGraphic);
	newImageDiv.classList.add("image" + id);
	tileImageContainer.appendChild(newImageDiv);
	maxTile+=1;
}

function goToTile(newTileNumber){
	document.documentElement.scrollTop = 600 * newTileNumber;
	//highlightTile(newTileNumber);
}

function listUp(){
	if (currentTileNumber > 0){
		goToTile(currentTileNumber - 1);
	}
}
function listDown(){
	if (currentTileNumber < 4){
		goToTile(currentTileNumber + 1);
	}
}

function highlightTile(newTileNumber){
		var oldTile = document.querySelector(".tile" + currentTileNumber);
		var newTile = document.querySelector(".tile" + newTileNumber);
		var oldImage = document.querySelector(".image" + currentTileNumber);
		var newImage = document.querySelector(".image" + newTileNumber);
		const fadeSpeed = 35	;
		const minOpacity = 0.2;
		const maxOpacity = 1;
		var oldOpacity = maxOpacity;
		if (newTileNumber != currentTileNumber){
			const intervalId = setInterval(() => {
		        if (oldOpacity > minOpacity) {
		            oldOpacity -= 0.1;
		            oldTile.style.opacity = oldOpacity;
					oldImage.style.opacity = oldOpacity;
		        } else {
		            clearInterval(intervalId);
		        }
		    }, fadeSpeed);
			
		}
		var newOpacity = minOpacity;
		const intervalId1 = setInterval(() => {
			if (newOpacity < maxOpacity) {
				newOpacity += 0.1;
				newTile.style.opacity = newOpacity;
				newImage.style.opacity = newOpacity;
			} else {
				clearInterval(intervalId1);
				        }
		}, fadeSpeed);
		currentTileNumber = newTileNumber;
}

function autoHighlightTile(){
	var currScroll = window.scrollY + 350;
	var adjustedScroll = currScroll - currScroll % 600;
	var closestTile = adjustedScroll / 600;
	if (closestTile != currentTileNumber){
		highlightTile(closestTile);
	}
}

var minTile = 0;
var maxTile = 0;
generateTile("Overview", "Lorem ipsum dolor sit amet", 0);
generateTile("Overview", "Lorem ipsum dolor sit amet", 1);
generateTile("Overview", "Lorem ipsum dolor sit amet", 2);
generateTile("Overview", "Lorem ipsum dolor sit amet", 3);
generateTile("Overview", "Lorem ipsum dolor sit amet", 4);
var currentTileNumber = 0;
goToTile(0);
document.documentElement.scrollTop = 0;
addEventListener("scroll", (event) => {autoHighlightTile()});
highlightTile(0);