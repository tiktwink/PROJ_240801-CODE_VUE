const divideContentToSections = (html, symbol) => {
  let sections = []
  let item = {}
  let response = {}
  let firstPartIsSectionContentNotPartContent = false
  
  let si = html.indexOf(symbol)
  if (si === -1) {
    response.isSectioned = false
    response.sectionContent = html
    response.sections = sections
    return response
  }
  
  response.isSectioned = true
  response.sectionContent = html //当文章已分节时，是否要将整篇文章内容添加为返回体一级属性，注释掉表示不添加
  let sj = html.lastIndexOf('<', si) //当前#!所在标签头的左<
  if (sj !== 0) { //章首存在一个没有节标题的节
    if (symbol === '#*') {
      firstPartIsSectionContentNotPartContent = true //对节分段时，第一段如果没有#*标记，则不能像对章分节那样处理，而应该依旧把这段标记为节而非段，这一部分是节的正文
    }
    let sectionContent = html.slice(0, sj)
    item.sectionOrder = sections.length + 1
    item.sectionTitle = 'noneTitle'
    // item.sectionTitle = 'none' + sections.length + 1
    item.sectionContent = sectionContent
    sections.push(item)
    item = {}
    html = html.slice(sj)
  }
  
  si = html.indexOf(symbol)
  let sii = 0
  let sjj = 0
  while (si !== -1) {
    
    sj = html.indexOf('<', si) //当前si所指#!所在标签尾的左<
    item.sectionOrder = sections.length + 1
    item.sectionTitle = html.slice(si + 2, sj).trim()
    sj = html.indexOf('>', si) //当前si所指#!所在标签尾的右>
    sii = html.indexOf(symbol, si + 1)
    if (sii === -1) { //当前si为最后一个节的节首
      // console.log('sii===-1')
      item.sectionContent = html.slice(sj + 1)
      sections.push(item)
      item = {}
      si = -1 //终止匹配过程
    } else { //当前si不是最后一个节的节首
      sjj = html.lastIndexOf('<', sii)//当前sii所指#!所在标签头的左<
      // console.log(sjj)
      item.sectionContent = html.slice(sj + 1, sjj)
      sections.push(item)
      item = {}
      html = html.slice(sii)
      si = html.indexOf(symbol)
    }
  }
  
  // 解析标题
  sections = sections.map((item, index) => {
    return {
      ...item,
      ...viewSectionTitle(item.sectionTitle, symbol)
    }
  })
  //处理节中无标记的第一段（将其标记为article-section而非article-part
  if (symbol === '#*' && firstPartIsSectionContentNotPartContent) {
    let sectionClass = sections[0].sectionClass
    console.log(sectionClass)
    console.log('123165')
    sectionClass.push('article-section')
    console.log(sectionClass)
    sectionClass.splice(sectionClass.indexOf('article-part'), 1)
    console.log(sectionClass)
    sections[0].sectionClass = sectionClass
  }
  
  
  response.sections = sections
  response.sectionCount = sections.length
  return response
}

//解析标题（节、段）
const viewSectionTitle = (sectionTitle, symbol) => {
  let sectionClass = []
  if (symbol === '#!') {
    sectionClass = ['article-section']
    
    if (sectionTitle.includes('#1')) {
      sectionClass.push('article-section-fantacy')
      sectionTitle = sectionTitle.replace('#1', '')
    }
    if (sectionTitle.includes('#2')) {
      sectionClass.push('article-section-colorful')
      sectionTitle = sectionTitle.replace('#2', '')
    }
    if (sectionTitle.includes('/1')) {
      sectionClass.push('article-section-pre')
      sectionTitle = sectionTitle.replace('/1', '')
    }
    if (sectionTitle.includes('/2')) {
      sectionClass.push('article-section-ad')
      sectionTitle = sectionTitle.replace('/2', '')
    }
  } else if (symbol === '#*')
    sectionClass = ['article-part']
  
  // console.log('class: ' + sectionClass)
  return {
    sectionTitle: sectionTitle,
    sectionClass: sectionClass
  }
}

//默认导出
const divideToSections = (html) => {
  let response = divideContentToSections(html, '#!') //解析节标题时，symbol为#!
  if (!response.isSectioned) {
    return response
  }
  let sections = response.sections
  sections = sections.map((item, index) => {
    return {
      ...item,
      ...divideContentToSections(item.sectionContent, '#*'), //解析段标题时，symbol为#*
    }
  })
  response.sections = sections
  return response
}

export default divideToSections
