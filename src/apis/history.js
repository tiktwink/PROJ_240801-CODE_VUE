import useUserStore from "@/stores/useUserStore.js";
import useHistoryStore from "@/stores/useHistoryStore.js";

const username = useUserStore().username
const checker = ''
const historyStore = useHistoryStore()


const logCheck = (check, note = '') => {
  check.operatorId = username
  if (username.slice(0, 3) === '100' && username.length === 4) {
    check.checkerId = username
  } else {
    check.checkerId = '1001'
  }
  check.logTime = Date.now()
  check.note = note
  
  console.log('to log check: ', check)
  historyStore.checkLogs.unshift(check)
  
  
}


