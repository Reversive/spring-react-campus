import styled from "styled-components";

export const FileQueryContainer = styled.form`
  display: flex;
  flex-direction: column;
  background: ${(props) => props.theme.cyanDarkest};
  padding: 12px;
  margin-bottom: 20px;
  border-radius: 12px;
`;

export const FileQueryContainerDiv = styled.div`
  display: flex;
  flex-direction: column;
  background: ${(props) => props.theme.cyanDarkest};
  padding: 12px;
  margin-bottom: 20px;
  border-radius: 12px;
`;

export const FileFilterContainer = styled.div`
  display: flex;
  padding: 0 30px;
  flex-direction: column;
`;

export const FileSelectLabel = styled.label`
  font-size: 18px;
  font-weight: 700;
  color: ${(props) => props.theme.cyanLight};
`;

export const FileSelect = styled.select`
  font-size: 18px;
  border-radius: 12px;
  background: ${(props) => props.theme.cyanLight};
  padding: 5px;
  outline: none;
  border: none;
`;

export const FileCheckboxLabel = styled.label`
  color: ${(props) => props.theme.cyanLight};
`;

export const FileCheckbox = styled.input`
  border: 1px solid ${(props) => props.theme.cyanLight};
`;

export const PaginationArrow = styled.img`
  cursor: pointer;
  margin: 0 8px;
  height: 36px;
`;

export const FileFilterPill = styled.div<{ red: boolean }>`
  color: white;
  font-weight: 700;
  border-radius: 12px;
  padding: 4px 8px;
  margin-left: 6px;
  margin-bottom: 2px;
  background: ${(props) => (props.red ? "#a80011" : props.theme.cyanDark)};
`;
