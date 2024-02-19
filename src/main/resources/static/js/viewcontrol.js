
function hideSearchResultElement() {

    let searchResultDiv = document.getElementById('searchResults');
      
    while (searchResultDiv.firstChild) {
        searchResultDiv.removeChild(searchResultDiv.firstChild);
    }

    searchResultDiv.classList.remove('searchResultsStyles');

}


function displaySearchResults(resultList) {

    hideSearchResultElement();

    let searchResultDiv = document.getElementById('searchResults');
    searchResultDiv.classList.add('searchResultsStyles');

    if(resultList.length > 0) {

        // subtitle
        let searchResultsTitle = document.createElement('p');
        searchResultsTitle.classList.add('dataSheetSubtitle');
        searchResultsTitle.textContent = 'Keresési találatok:';
        searchResultDiv.appendChild(searchResultsTitle);

        // result list
        let canOverflowDiv = document.createElement('div');
        canOverflowDiv.classList.add('canOverflow');
        searchResultDiv.appendChild(canOverflowDiv);

        let listWrapperElement = document.createElement('ul');

        for(let [index, node] of resultList.entries()) {

            let listElement = document.createElement('li');
            listElement.textContent = buildIndividualsNameString(node);
            listElement.classList.add('clickable');

            if(index % 2 == 1) {
                listElement.style.backgroundColor = '#a1cece';
            }

            listElement.addEventListener('click', () => {
                centerViewAtNode(node);
                highlightNode(node);
                getIndividualsFullData(node.id);
            });
            
            listWrapperElement.appendChild(listElement);
        }

        canOverflowDiv.appendChild(listWrapperElement);
      
    }
    else {

      let errorMessageElement = document.createElement('p');
      errorMessageElement.textContent = "A keresés nem vezetett eredményre.";
      errorMessageElement.style.fontStyle = "italic";
      searchResultDiv.appendChild(errorMessageElement);
    }

    // close button
    let closeButtonContainer = document.createElement('div');
    closeButtonContainer.classList.add('searchResultsButtonContainer');
    searchResultDiv.appendChild(closeButtonContainer);

    let closeButton = document.createElement('button');
    closeButton.classList.add('button');
    closeButton.addEventListener('click', hideSearchResultElement);
    closeButton.textContent = '×';
    closeButtonContainer.appendChild(closeButton);

}

function clearIndividualsDataSheet() {

    let dataSheetDivHTML = document.getElementById('dataSheet');

    while(dataSheetDivHTML.firstChild) {
      dataSheetDivHTML.removeChild(dataSheetDivHTML.firstChild);
    }
    
    dataSheetDivHTML.classList.remove('dataSheetStyles');
}

function displayIndividualsDatasheet(fullData) {

    clearIndividualsDataSheet();

    let dataSheetDivHTML = document.getElementById('dataSheet');
    dataSheetDivHTML.classList.add('dataSheetStyles');

    // top-right buttons
    let buttonContainer = document.createElement('div');
    buttonContainer.classList.add('dataSheetButtonContainer');
    dataSheetDivHTML.appendChild(buttonContainer);

    let closeButton = document.createElement('button');
    closeButton.classList.add('button');
    closeButton.addEventListener('click', clearIndividualsDataSheet)
    closeButton.textContent = '×';
    buttonContainer.appendChild(closeButton);

    let nextButton = document.createElement('button');
    nextButton.textContent = '>';
    buttonContainer.appendChild(nextButton);
    if(nextDataSheets.size > 0)  {
      nextButton.classList.add('button');
      nextButton.addEventListener('click', () => {
        previousDataSheets.push(fullData);
        displayIndividualsDatasheet(nextDataSheets.pop());
      });
    }
    else {
      nextButton.classList.add('buttonInactive');
    }

    let prevButton = document.createElement('button');
    prevButton.textContent = '<';
    buttonContainer.appendChild(prevButton);
    if(previousDataSheets.size > 1 || (previousDataSheets.size == 1 && fullData.id != previousDataSheets.peek().id)) {
      prevButton.classList.add('button');
      prevButton.addEventListener('click', () => { 
        
        nextDataSheets.push(fullData);

        while(fullData.id == previousDataSheets.peek().id) {
          previousDataSheets.pop();
        }

        recentDatasheet = previousDataSheets.pop();
        displayIndividualsDatasheet(recentDatasheet)
      });
    }
    else {
      prevButton.classList.add('buttonInactive');
    }

    // name
    let nameElement = document.createElement('h2');
    nameElement.style.marginTop = '0';
    nameElement.style.marginBottom = '3px';
    nameElement.style.cursor = 'pointer';
    nameElement.textContent = fullData.name;
    nameElement.addEventListener('click', () => {
      let node = individualMapById.get(fullData.id);
      centerViewAtNode(node);
      highlightNode(node);
    });
    dataSheetDivHTML.appendChild(nameElement);
    // gender
    let genderAndLifetimeText = '';
    if(fullData.gender == 'F') {
      genderAndLifetimeText += 'nő, ';
    }
    else if(fullData.gender == 'M') {
      genderAndLifetimeText += 'férfi, ';
    }
    // birth and death dates
    if(fullData.birth != null && fullData.birth.length > 0) {
      genderAndLifetimeText += fullData.birth + ' -';
    }
    else {
      genderAndLifetimeText += '?  -'
    }

    if(fullData.death != null && fullData.death.length > 0) {
      genderAndLifetimeText += ' ' + fullData.death;
    }
    else {
      genderAndLifetimeText += '  ?'
    }

    let genderAndLifeTextElement = document.createElement('p');
    genderAndLifeTextElement.style.fontStyle = 'italic';
    genderAndLifeTextElement.style.marginTop = '0px';
    genderAndLifeTextElement.style.marginBottom = '4px';
    genderAndLifeTextElement.textContent = genderAndLifetimeText;
    dataSheetDivHTML.appendChild(genderAndLifeTextElement);

    let canOverflowDiv = document.createElement('div');
    canOverflowDiv.classList.add('canOverflow');
    dataSheetDivHTML.appendChild(canOverflowDiv);

    // additional information about the person
    if(fullData.personalComment != null && fullData.personalComment.length > 0) {
      let commentTitleElement = document.createElement('p');
      commentTitleElement.classList.add('dataSheetSubtitle');
      commentTitleElement.textContent = 'Információ:';
      canOverflowDiv.appendChild(commentTitleElement);

      let commentElement = document.createElement('p');
      commentElement.style.marginTop = '3px';
      commentElement.textContent = fullData.personalComment;
      canOverflowDiv.appendChild(commentElement);
    }
    // close family lists
    if(fullData.families != null && fullData.families.length > 0) {
      let familiesTitleElement = document.createElement('p');
      familiesTitleElement.classList.add('dataSheetSubtitle');
      familiesTitleElement.textContent = 'Közvetlen hozzátartozók:';
      canOverflowDiv.appendChild(familiesTitleElement);

      let listOfFamiliesElement = document.createElement('dl');
      for(const family of fullData.families) {

        // family wrapper div for styling purposes
        let familyWrapperDiv = document.createElement('div');
        familyWrapperDiv.classList.add('familyWrapper');
        listOfFamiliesElement.appendChild(familyWrapperDiv);
        
        let familyParentElement = document.createElement('dt');
        for(let i = 0; i < family.parents.length; i++) {
          // parent names in this family
          let parentSpanElement = document.createElement('span');
          parentSpanElement.textContent = family.parents[i].name;
          parentSpanElement.classList.add('clickable');
          parentSpanElement.classList.add('parentInFamily');
          parentSpanElement.addEventListener('click', () => {
            let node = individualMapById.get(family.parents[i].id);
            centerViewAtNode(node);
            highlightNode(node);
          });
          familyParentElement.appendChild(parentSpanElement);

          if(i < family.parents.length -1) {
            let comaSpanElement = document.createElement('span');
            comaSpanElement.textContent = ', ';
            familyParentElement.appendChild(comaSpanElement);
          }
        }
        // marriage date
        if(family.marriageDate != null && family.marriageDate.length > 0) {
          let marriageDateElement = document.createElement('p');
          marriageDateElement.style.fontSize = 'smaller';
          marriageDateElement.style.margin = '0';
          marriageDateElement.textContent = 'Házasság dátuma / kapcsolat kezdete: ' + family.marriageDate;
          familyParentElement.appendChild(marriageDateElement);
        }
        // marriage comment
        if(family.comments != null && family.comments.length > 0) {
          let marriageCommentElement = document.createElement('p');
          marriageCommentElement.style.fontSize = 'smaller';
          marriageCommentElement.style.margin = '0';
          marriageCommentElement.textContent = family.comments;
          familyParentElement.appendChild(marriageCommentElement);
        }
        
        familyWrapperDiv.appendChild(familyParentElement);

        // children 
        if(family.children != null && family.children.length > 0) {
          let familyChildrenElement = document.createElement('dd');
          for(let i = 0; i < family.children.length; i++) {
            // children names in the family
            let childSpanElement = document.createElement('span');
            childSpanElement.textContent = family.children[i].name;
            childSpanElement.classList.add('clickable');
            childSpanElement.classList.add('childInFamily');
            childSpanElement.addEventListener('click', () => {
              let node = individualMapById.get(family.children[i].id);
              centerViewAtNode(node);
              highlightNode(node);
            });
            familyChildrenElement.appendChild(childSpanElement);

            if(i < family.children.length -1) {
              let comaSpanElement = document.createElement('span');
              comaSpanElement.textContent = ', ';
              familyChildrenElement.appendChild(comaSpanElement);
            }
          }
          familyWrapperDiv.appendChild(familyChildrenElement);
        }           
      }

      canOverflowDiv.appendChild(listOfFamiliesElement);
    }
}