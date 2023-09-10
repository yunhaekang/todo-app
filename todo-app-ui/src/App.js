import "./App.css";
import Todo from "./Todo";
import React, { useEffect, useState } from "react";
import { Container, List, Paper, Grid, Button, AppBar, Toolbar, Typography } from "@mui/material";
import AddTodo from "./AddTodo";
import { call, signout } from "./service/ApiService";

function App() {
  
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  // useEffect는 함수와 배열을 인자로 받는다. (콜백함수, 디펜던시 배열)
  // useEffect는 첫 렌더링(또는 마운팅)이 일어났을 때, 그 이후에는
  // 디펜던시 배열 내의 값이 변할 때마다 콜백 함수를 부른다.
  // 무한 루프 방지를 위해서 빈 배열을 넘겼다.
  useEffect(() => {
    call("/todo", "GET", null)
      .then((response) => {
        setItems(response.data);
        setLoading(false);
      });
  }, []);
  

  /* 전체 Todo 리스트는 이곳에서 관리하기 때문에 추가/삭제 함수를 여기에 작성해야 함 */
  // 투두 추가를 위한 함수 작성 
  const addItem = (item) => {
    /* 백엔드 연결 이전 작성 코드
    item.id = "ID-" + items.length; // key를 위한 id
    item.done = false; // done 초기화
    // 업데이트는 반드시 setItems로 하고 새 배열을 만들어야 한다.
    setItems([...items, item]);
    console.log("items: ",  items);
    */

    call("/todo", "POST", item).then((response) => setItems(response.data));
  };

  // 투두 수정을 위한 함수 작성
  const editItem = (item) => {
    /* 백엔드 연결 이전 작성 코드
    setItems([...items]);
    */

    call("/todo", "PUT", item).then((response) => setItems(response.data)); 
  };

  // 투두 삭제를 위한 함수 작성
  const deleteItem = (item) => {
    /* 백엔드 연결 이전 작성 코드
    // 삭제할 아이템을 제외한 아이템들을 찾는다.
    const remainedItems = items.filter(e => e.id !== item.id);
    // 삭제할 아이템을 제외한 아이템을 다시 배열에 저장한다.
    setItems([...remainedItems]);
    */
    call("/todo", "DELETE", item).then((response) => setItems(response.data));
  };

  let todoItems = items.length > 0 && (
    <Paper style={{ margin: 16 }}>
      <List>
        {items.map((item) => (
          <Todo 
            item={item} 
            key={item.id} 
            editItem={editItem} 
            deleteItem={deleteItem} 
          />
        ))}
      </List>
    </Paper>
  );

  // navigationBar 추가
  let navigationBar = (
    <AppBar position="static">
      <Toolbar>
        <Grid justifyContent={"space-between"} container>
          <Grid item>
            <Typography variant="h6">오늘의 할 일</Typography>
          </Grid>
          <Grid item>
            <Button color="inherit" raised onClick={signout}>
              로그아웃
            </Button>
          </Grid>
        </Grid>
      </Toolbar>
    </AppBar>
  );

  /* 로딩 중이 아닐 때 렌더링할 부분 */
  let todoListPage = (
    <div>
      {navigationBar}
      <Container maxWidth="md">
        <AddTodo addItem={addItem} />
        <div className="TodoList">{todoItems}</div>
      </Container>
    </div>
  );

  /* 로딩 중일 때 렌더링 할 부분 */
  let loadingPage = <h1> 로딩 중 ... </h1>;
  let content = loadingPage;

  if(!loading) {
    // 로딩 중이 아니면 todoListPage를 선택
    content = todoListPage;
  }

  /* 선택한 content 렌더링 */
  return (
    <div className="App">
      {content}
    </div>
  );
}

export default App;
