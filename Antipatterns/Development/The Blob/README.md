# The Blob (God's class)

**A class grown large and with big complexity becomes centralized for many functionalities, leading to problems with the code organization, maintainability and clear responsibilities**

***

*symptos*

+ single class with enormous amount of attributes and operations (becomes the blob)

+ single class containing unrelated attributes and operations (classes like Utility or Common)

+ single controller

+ hard scalability due to strong cohasion between the blob and other classes

+ hard testing

*causes*

+ lack the skills to create good abstraction

+ not creating new objects while extending the functionality

+ lack of architecture

*solution*

+ add good abstraction, devide the responsibilities and make the structure flexible and simple