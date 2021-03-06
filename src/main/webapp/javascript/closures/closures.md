#### 클로저

클로저는 내부함수가 외부함수의 맥락(context)에 접근할 수 있는 것을 가리킨다. 자바스크립트에서는 중첩된 함수들의 외부 유효 범위에서 선언된 변수들에 접근할 권할을 가진다.

```javascript
function makeFunc() {
    var name = "Mozilla";	
    function displayName() { // 내부 함수이며,클로저
        alert(name);	// 부모 함수에서 선언된 변수를 사용
    }
    return displayName;
}

var myFunc = makeFunc();
myFunc();
```





#### 일반적인 실수

루프 안에서 클로저가 생성되었을 때 다음과 같은 일반적인 문제가 발생한다. 다음 예제를 보자.

```html
<!-- 도움말 힌트 -->
<p id="help">Helpful notes will appear here</p>
<!-- 입력 필드 -->
<p>E-mail: <input type="text" id="email" name="email"></p>
<p>Name: <input type="text" id="name" name="name"></p>
<p>Age: <input type="text" id="age" name="age"></p>
```

```javascript
function showHelp(help) {
    document.getElementById('help').innerHTML = help;
}

function setupHelp() {
    var helpText = [
        {'id': 'email', 'help': 'Your e-mail address'},
        {'id': 'name', 'help': 'Your full name'},
        {'id': 'age', 'help': 'Your age (you must be over 16)'}
    ];

    for (var i = 0; i < helpText.length; i++) {
        var item = helpText[i];
        document.getElementById(item.id).onfocus = function() {
            showHelp(item.help);
        }
    }
}

setupHelp();
```

[결과보기](https://jy836979.github.io/Tutorials/src/main/webapp/javascript/closures/closures-general-miss.html)

setupHelp 함수 내부를 보면 루프를 돌면서 각 입력 필드 ID에 해당하는 엘리먼트의 onfocus 이벤트에 관련된 도움말을 보여주는 메소드를 연결한다. 

이 코드를 사용하면 제대로 작동하지 않는 것을 알게 된다. 어떤 필드에 포커스를 주더라도 나이에 관한 도움말이 표시된다.

onfocus 이벤트에 연결된 함수가 클로저이기 때문이다. 이 클로저는 setupHelp 함수가 실행될 때 생성된 내부함수로 외부함수의 지역변수를 참조한다. 
루프에서 세 개의 클로저가 만들어졌지만 각 클로저는 값이 변하는 지역변수 item을 공유한다. 
onfocus 콜백이 실행될 때  콜백 환경에서 item은 helpText 리스트의 마지막 요소를 가리키고 있을 것이다.

이 경우 한 가지 해결책은 더 많은 클로저를 사용하는 것이다.

```javascript
function showHelp(help) {
    document.getElementById('help').innerHTML = help;
}
// 함수 팩토리 사용
function makeHelpCallback(help) {
    return function() {
      showHelp(help);
    };
}

function setupHelp() {
    var helpText = [
        {'id': 'email', 'help': 'Your e-mail address' },
        {'id': 'name', 'help': 'Your full name'},
        {'id': 'age', 'help': 'Your age (you must be over 16)'}
    ];

    for (var i = 0; i < helpText.length; i++) {
        var item = helpText[i];
        document.getElementById(item.id).onfocus = makeHelpCallback(item.help);
    }
}

setupHelp();
```

[결과보기](https://jy836979.github.io/Tutorials/src/main/webapp/javascript/closures/closures-general-miss-solution-1.html)

모두 단일 환경을 공유하는 콜백대신, makeHelpCallback 함수는 각각의 콜백에 새로운 어휘적 환경을 생성한다. 
여기서 help는 helpText 배열의 해당 문자열을 나타낸다.

함수 팩토리를 사용하지 않고 익명 클로저를 사용하여 위 코드를 장성 할 수 있다.

```javascript
function showHelp(help) {
    document.getElementById('help').innerHTML = help;
}

function setupHelp() {
    var helpText = [
        {'id': 'email', 'help': 'Your e-mail address' },
        {'id': 'name', 'help': 'Your full name'},
        {'id': 'age', 'help': 'Your age (you must be over 16)'}
    ];

    for (var i = 0; i < helpText.length; i++) {
        (function() {
            var item = helpText[i];
            document.getElementById(item.id).onfocus = function() {
				showHelp(item.help);
            };
        })();	// 즉시실행 함수 사용(IIFE: Immediately Invoked Function Expresion)
    }
}

setupHelp();
```

즉시실행 함수로 클로저를 감싸면 각각의 콜백에 새로운 어휘적 환경을 생성할 수 있다.

더 많은 클로저를 사용하는 것이 싫다면 ES2015의 let 키워드를 사용할 수 있다.

```javascript
function showHelp(help) {
    document.getElementById('help').innerHTML = help;
}

function setupHelp() {
    var helpText = [
        {'id': 'email', 'help': 'Your e-mail address'},
        {'id': 'name', 'help': 'Your full name'},
        {'id': 'age', 'help': 'Your age (you must be over 16)'}
    ];

    for (var i = 0; i < helpText.length; i++) {
        // let 문은 Internet Explorer 11 표준 부터 지원
        let item = helpText[i];
        document.getElementById(item.id).onfocus = function() {
            showHelp(item.help);
        }
    }
}
```

[결과보기](https://jy836979.github.io/Tutorials/src/main/webapp/javascript/closures/closures-general-miss-solution-2.html)

위의 경우 var 대신 let을 사용하여 모든 클로가 블록 범위 변수를 바인딩 할 것이므로 추가적인 클로저를 사용하지 않아도 완벽하게 동작할 것이다.





#### 성능 관련 고려 사항

특정 작업에 클로저가 필요하지 않는데 다른 함수 내에서 함수를 불필요하게 작성하는 것은 현명하지 않다. 이것은 처리 속도와 소비 측면에서 스크립트 성능에 부정적인 영향을 미칠 것이다.

예를 들어, 새로운 객체/클래스를 생성 할 때, 메소드는 일반적으로 객체 생성자에 정의되기보다는 객체의 프로토 타입에 연결되어야 한다. 그 이유는 생성자가 호출 될 때마다 메서드가 다시 할당되기 때문이다. (즉, 모든 개체가 생성 될 때마다)

비실용적이지만 시범적인 다음 예를 고려하라:

```javascript
function MyObject(name, message) {
    this.name = name.toString();
    this.message = message.toString();
    this.getName = function() {
        
    };
    this.getMessage = function() {
        
    }
}
```

앞의 코드는 클로저의 이점을 이용하지 않음으 다음과 같이 다시 쓸 수 있다.

```javascript
function MyObject(name, message) {
    this.name = name.toString();
    this.message = message.toString();
}
MyObject.prototype = {
    getName: function() {
        return this.name;
    },
    getMessage: function() {
        return this.message;
    }
};
```

그러나 프로토타입을 다시 정의하는 것은 권장되지 않음으로 기존 프로토타입에 추가하는 다음 예제가 더 좋다.

```javascript
function MyObject(name, message) {
    this.name = name.toString();
    this.message = message.toString();
}
MyObject.prototype.getName = function() {
    return this.name;
};
MyObject.prototype.getMessage = function() {
    return this.message;
};
```

위의 코드와 같은 결과를 가진 더 깨긋한 방법으로 작성할 수도 있다:

```javascript
function MyObject(name, message) {
    this.name = name.toString();
    this.message = message.toString();
}
(function() {
    this.getName = function() {
        return this.name;
    };
    this.getMessage = function() {
		return this.message;  
    };
}).call(MyObject.prototype);
```

앞의 두 가지 예제에서 상속된 프로토타입은 모든 객체에서 공유될 수 있으며 메소드 정의는 모든 객체 생성시 발생할 필요가 없다.

#### 참고

https://developer.mozilla.org/ko/docs/Web/JavaScript/Guide/Closures

https://opentutorials.org/course/743/6544





