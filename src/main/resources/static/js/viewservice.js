
function searchIndividualByFullName(keyword) {     
  let resultList = [];
  const individualMapKeys = individualMapByName.keys();
  
  for(let namekey of individualMapKeys) {

    if(namekey.includes(keyword)) {
      resultList.push( individualMapByName.get(namekey) );
    }
  }

  return resultList;
}

function searchIndividualByPartsOfTheirName(keywords) {
  let resultList = [];
  const individualMapKeys = individualMapByName.keys();
  
  for(let namekey of individualMapKeys) {

    let matchFound = true;

    keywords.forEach(keyword => {
      if(namekey.includes(keyword) === false) {
        matchFound = false;
      }
    });

    if(matchFound === true) {
      resultList.push( individualMapByName.get(namekey) );
    }
  }

  return resultList;
}

function searchIndividual(keyword) {

  let resultList = searchIndividualByFullName(keyword);

  if(resultList.length === 0) {
    const keywordSubstrings = keyword.split(' ');

    if(keywordSubstrings.length > 1) {
      resultList = searchIndividualByPartsOfTheirName(keywordSubstrings);
    }
  }

  displaySearchResults(resultList);
}

function buildIndividualsNameString(node) {
  
  let individualsName = node.name + " (";
      
  if(node.birth != null) {
    individualsName += node.birth;
  }
  else {
    individualsName += " ";
  }

  individualsName += " - ";
  
  if(node.death != null) {
    individualsName += node.death;
  }
  else {
    individualsName += " ";
  }

  individualsName += ")";

  return individualsName;
}

function saveDatasheetToRecentlyVisitedStack(dataSheet) {
  // add any next datasheets from the next datasheets stack
  for(let i = nextDataSheets.size; i > 0; i--) {
    previousDataSheets.push( nextDataSheets.pop() );
  }

  // add the cached datasheet to previous stack (typically cached after a previous.pop())
  if(recentDatasheet != null && recentDatasheet.id != dataSheet.id) {
    previousDataSheets.push(recentDatasheet);
    recentDatasheet = null;
  }

  // add the datasheet from the current request to previous stack
  if(previousDataSheets.peek() != null) {      

    if(previousDataSheets.peek().id != dataSheet.id) {
      previousDataSheets.push(dataSheet);
    }
  }
  else {
    previousDataSheets.push(dataSheet);
  }
}

function getIndividualsFullData(nodeId) {

  fetch("/protected/api/individual/" + nodeId, {
        credentials: "include"
      })
    .then(response => response.json())
    .then(json => {
      saveDatasheetToRecentlyVisitedStack(json);
      displayIndividualsDatasheet(json);
    });
}
