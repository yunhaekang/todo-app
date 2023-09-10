import { API_BASE_URL  } from "../api-config";

export function call(api, method, request) {
    let headers = new Headers({
        "Content-Type": "application/json",
    });

    // 로컬 스토리지에서 ACCESS_TOKEN 가져오기
    const accessToken = localStorage.getItem("ACCESS_TOKEN");
    if(accessToken && accessToken !== null) {
        headers.append("Authorization", "Bearer " + accessToken);
    }

    let options = {
        headers: headers,
        url: API_BASE_URL + api,
        method: method,
    };

    if(request) {
        // GET method
        options.body = JSON.stringify(request);
    }

    return fetch(options.url, options).then((response) => {
        if (response.status === 200) {
            return response.json();
        } else if (response.status === 403) {
            // 권한 없음 응답을 받은 경우 login 페이지로 redirect 처리
            window.location.href = "/login";
        } else {
            new Error(response); 
        }
    }).catch((error) => {
        console.log("http error");
        console.log(error);
    });
}

export function signup(userDTO) {
    return call("/user/signup", "POST", userDTO);
}

export function signin(userDTO) {
    return call("/user/signin", "POST", userDTO)
        .then((response) => {
            if(response.token) {
                // token이 존재하는 경우 
                // 로컬 스토리지에 토큰 저장
                localStorage.setItem("ACCESS_TOKEN", response.token);
                // Todo 화면으로 리디렉트 처리
                window.location.href = "/";
            }
        });
}

export function signout() {
    localStorage.setItem("ACCESS_TOKEN", null);
    window.location.href = "/login";
}
