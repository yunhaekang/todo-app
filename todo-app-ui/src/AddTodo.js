import React, { useState } from "react";
import { Button, Grid, TextField } from "@mui/material";

const AddTodo = (props) => {
    // 사용자의 입력을 저장할 오브젝트
    const [item, setItem] = useState({ title: ""});
    
    const addItem = props.addItem; // App.js에 선언한 함수

    // title 입력 감지 함수 작성
    const onInputChange = (e) => {
        setItem({title: e.target.value});
        console.log(item);
    }

    // 추가 버튼 클릭 함수 작성
    const onButtonClick = () => {
        addItem(item);  // addItem 함수 사용
        setItem({ title: "" });
    }

    // 엔터 키 이벤트 핸들러 함수
    const enterKeyEventHandler = (e) => {
        if(e.key === 'Enter') {
            onButtonClick();
        }
    }

    return (
        <Grid container style={{ marginTop: 20}}>
            <Grid xs={11} md={11} item style={{ paddingRight: 16 }}>
                <TextField placeholder="Add Todo here" fullWidth 
                    onChange={onInputChange} 
                    onKeyDown={enterKeyEventHandler}
                    value={item.title} />
            </Grid>
            <Grid xs={1} md={1} item>
                <Button fullWidth style={{ height: '100%' }} color="secondary" variant="outlined"
                onClick={onButtonClick}>
                    추가
                </Button>
            </Grid>
        </Grid>
    );
}

export default AddTodo;