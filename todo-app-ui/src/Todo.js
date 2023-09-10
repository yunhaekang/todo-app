import React, { useState, useEffect } from "react";
import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton } from "@mui/material";
import DeleteOutlined from "@mui/icons-material/DeleteOutlined";

const Todo = (props) => {
    // eslint-disable-next-line
    const [item,setItem] = useState(props.item);
    const [readOnly, setReadOnly] = useState(true);

    const deleteItem = props.deleteItem; // App.js에 선언한 함수
    const editItem = props.editItem;

    // 수정 이벤트 핸들러 작성
    const editEventHandler = (e) => {
        setItem({...item, title: e.target.value});
    };

    const checkboxEventHandler = (e) => {
        item.done = e.target.checked;
        editItem(item);
    };
    // 삭제 이벤트 핸들러 작성
    const deleteEventHandler = () => {
        deleteItem(item);
    };

    // 읽기 전용 모드 끄기
    const turnOffReadOnly = () => {
        setReadOnly(false);
    };

    // 읽기 전용 모드 켜기
    const turnOnReadOnly = (e) => {
        if(e.key === "Enter" && readOnly === false) {
            setReadOnly(true);
            // 이 때에만 서버로 수정 요청을 보내도록 함.
            editItem(item);
        }
    };

    

    return (
        <ListItem>
            <Checkbox checked={item.done} onChange={checkboxEventHandler} />
            <ListItemText>
                <InputBase
                    inputProps={{ "aria-label": "naked",
                                    readOnly: readOnly }}
                    onClick={turnOffReadOnly}
                    onKeyDown={turnOnReadOnly}
                    onChange={editEventHandler}
                    type="text"
                    id={item.id} 
                    name={item.id}
                    value={item.title}
                    multiline={true}
                    fullWidth={true}
                />
            </ListItemText>
            <ListItemSecondaryAction>
                <IconButton aria-label="Delete Todo" onClick={deleteEventHandler} >
                    <DeleteOutlined />
                </IconButton>
            </ListItemSecondaryAction>
        </ListItem>
    );
};

export default Todo;