const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

const firestore = admin.firestore();

exports.modifyItem = functions.firestore
  .document("users/{userName}/entries/{entryId}")
  .onCreate(async (snapshot, context) => {
    const userName = context.params.userName;
    const entryId = context.params.entryId;

    // Get the inserted item
    const insertedItem = snapshot.data().item;

    // Apply the desired modifications
    const modifiedItem = modifyString(insertedItem);

    // Update the Firestore document with the modified item
    await firestore
      .doc(`users/${userName}/entries/${entryId}`)
      .update({ item: modifiedItem });

    return null;
  });

// Function to modify the string as required
function modifyString(string) {
  // Capitalize the first letter
  string = capitalizeFirstLetter(string);

  // Trim spaces around
  string = string.trim();

  // Remove continuous spaces between words
  string = string.replace(/\s+/g, " ");

  return string;
}

// Function to capitalize the first letter of a string
function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}
